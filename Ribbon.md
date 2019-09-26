####客户端负载平衡器：Ribbon<br>
Ribbon是一个客户端负载均衡器，它可以很好地控制HTTP和TCP客户端的行为。Feign已经使用Ribbon，所以如果您使用@FeignClient，则本节也适用。

Ribbon中的中心概念是指定客户端的概念。每个负载平衡器是组合的组合的一部分，它们一起工作以根据需要联系远程服务器，并且集合具有您将其作为应用程序开发人员（例如使用@FeignClient注释）的名称。Spring Cloud使用RibbonClientConfiguration为每个命名的客户端根据需要创建一个新的合奏作为ApplicationContext。这包含（除其他外）ILoadBalancer，RestClient和ServerListFilter。

####如何加入Ribbon
要在项目中包含Ribbon，请使用组org.springframework.cloud和工件ID spring-cloud-starter-ribbon的起始器。有关 使用当前的Spring Cloud发布列表设置构建系统的详细信息，请参阅Spring Cloud项目页面。

####自定义Ribbon客户端
您可以使用<client>.ribbon.*中的外部属性来配置Ribbon客户端的某些位，这与使用Netflix API本身没有什么不同，只能使用Spring Boot配置文件。本机选项可以在CommonClientConfigKey（功能区内核心部分）中作为静态字段进行检查。

Spring Cloud还允许您通过使用@RibbonClient声明其他配置（位于RibbonClientConfiguration之上）来完全控制客户端。例：

@Configuration
@RibbonClient(name = "foo", configuration = FooConfiguration.class)
public class TestConfiguration {
}
在这种情况下，客户端由RibbonClientConfiguration中已经存在的组件与FooConfiguration中的任何组件组成（后者通常会覆盖前者）。

####警告
FooConfiguration必须是@Configuration，但请注意，它不在主应用程序上下文的@ComponentScan中，否则将由所有@RibbonClients共享。如果您使用@ComponentScan（或@SpringBootApplication），则需要采取措施避免包含（例如将其放在一个单独的，不重叠的包中，或者指定要在@ComponentScan）。
Spring Cloud Netflix默认情况下为Ribbon（BeanType beanName：ClassName）提供以下bean：

IClientConfig ribbonClientConfig：DefaultClientConfigImpl

IRule ribbonRule：ZoneAvoidanceRule

IPing ribbonPing：NoOpPing

ServerList<Server> ribbonServerList：ConfigurationBasedServerList

ServerListFilter<Server> ribbonServerListFilter：ZonePreferenceServerListFilter

ILoadBalancer ribbonLoadBalancer：ZoneAwareLoadBalancer

ServerListUpdater ribbonServerListUpdater：PollingServerListUpdater

创建一个类型的bean并将其放置在@RibbonClient配置（例如上面的FooConfiguration）中）允许您覆盖所描述的每个bean。例：

@Configuration
public class FooConfiguration {
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
}
这用PingUrl代替NoOpPing。

使用属性自定义Ribbon客户端
从版本1.2.0开始，Spring Cloud Netflix现在支持使用属性与Ribbon文档兼容来自定义Ribbon客户端。

这允许您在不同环境中更改启动时的行为。

支持的属性如下所示，应以<clientName>.ribbon.为前缀：

NFLoadBalancerClassName：应实施ILoadBalancer

NFLoadBalancerRuleClassName：应实施IRule

NFLoadBalancerPingClassName：应实施IPing

NIWSServerListClassName：应实施ServerList

NIWSServerListFilterClassName应实施ServerListFilter

注意
在这些属性中定义的类优先于使用@RibbonClient(configuration=MyRibbonConfig.class)定义的bean和由Spring Cloud Netflix提供的默认值。
要设置服务名称users的IRule，您可以设置以下内容：

application.yml
users:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule
有关Ribbon提供的实现，请参阅Ribbon文档。

####在Eureka中使用Ribbon
当Eureka与Ribbon结合使用（即两者都在类路径上）时，ribbonServerList将被扩展为DiscoveryEnabledNIWSServerList，扩展名为Eureka的服务器列表。它还用NIWSDiscoveryPing替换IPing接口，代理到Eureka以确定服务器是否启动。默认情况下安装的ServerList是一个DomainExtractingServerList，其目的是使物理元数据可用于负载平衡器，而不使用AWS AMI元数据（这是Netflix依赖的）。默认情况下，服务器列表将使用实例元数据（如远程客户端集合eureka.instance.metadataMap.zone）中提供的“区域”信息构建，如果缺少，则可以使用服务器主机名中的域名作为代理用于区域（如果设置了标志approximateZoneFromHostname）。一旦区域信息可用，它可以在ServerListFilter中使用。默认情况下，它将用于定位与客户端相同区域的服务器，因为默认值为ZonePreferenceServerListFilter。默认情况下，客户端的区域与远程实例的方式相同，即通过eureka.instance.metadataMap.zone。

注意
设置客户端区域的正统“archaius”方式是通过一个名为“@zone”的配置属性，如果可用，Spring Cloud将优先使用所有其他设置（请注意，该键必须被引用）在YAML配置中）。
注意
如果没有其他的区域数据源，则基于客户端配置（与实例配置相反）进行猜测。我们将eureka.client.availabilityZones（从区域名称映射到区域列表），并将实例自己的区域的第一个区域（即eureka.client.region，其默认为“us-east-1”为与本机Netflix的兼容性）。
示例：如何使用Ribbon不使用Eureka
Eureka是一种方便的方式来抽象远程服务器的发现，因此您不必在客户端中对其URL进行硬编码，但如果您不想使用它，Ribbon和Feign仍然是适用的。假设您已经为“商店”申请了@RibbonClient，并且Eureka未被使用（甚至不在类路径上）。Ribbon客户端默认为已配置的服务器列表，您可以提供这样的配置

application.yml
stores:
  ribbon:
    listOfServers: example.com,google.com
####示例：在Ribbon中禁用Eureka使用
设置属性ribbon.eureka.enabled = false将明确禁用在Ribbon中使用Eureka。

application.yml
ribbon:
  eureka:
   enabled: false
直接使用Ribbon API
您也可以直接使用LoadBalancerClient。例：

public class MyClass {
    @Autowired
    private LoadBalancerClient loadBalancer;

    public void doStuff() {
        ServiceInstance instance = loadBalancer.choose("stores");
        URI storesUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
        // ... do something with the URI
    }
}
缓存Ribbon配置
每个Ribbon命名的客户端都有一个相应的子应用程序上下文，Spring Cloud维护，这个应用程序上下文在第一个请求中被延迟加载到命名的客户端。可以通过指定Ribbon客户端的名称，在启动时，可以更改此延迟加载行为，从而热切加载这些子应用程序上下文。

application.yml<br>
ribbon:<br>
  &nbsp;&nbsp;&nbsp;&nbsp;eager-load:<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;clients: client1, client2, client3<br>