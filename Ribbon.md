####�ͻ��˸���ƽ������Ribbon<br>
Ribbon��һ���ͻ��˸��ؾ������������Ժܺõؿ���HTTP��TCP�ͻ��˵���Ϊ��Feign�Ѿ�ʹ��Ribbon�����������ʹ��@FeignClient���򱾽�Ҳ���á�

Ribbon�е����ĸ�����ָ���ͻ��˵ĸ��ÿ������ƽ��������ϵ���ϵ�һ���֣�����һ�����Ը�����Ҫ��ϵԶ�̷����������Ҽ��Ͼ�����������ΪӦ�ó��򿪷���Ա������ʹ��@FeignClientע�ͣ������ơ�Spring Cloudʹ��RibbonClientConfigurationΪÿ�������Ŀͻ��˸�����Ҫ����һ���µĺ�����ΪApplicationContext����������������⣩ILoadBalancer��RestClient��ServerListFilter��

####��μ���Ribbon
Ҫ����Ŀ�а���Ribbon����ʹ����org.springframework.cloud�͹���ID spring-cloud-starter-ribbon����ʼ�����й� ʹ�õ�ǰ��Spring Cloud�����б����ù���ϵͳ����ϸ��Ϣ�������Spring Cloud��Ŀҳ�档

####�Զ���Ribbon�ͻ���
������ʹ��<client>.ribbon.*�е��ⲿ����������Ribbon�ͻ��˵�ĳЩλ������ʹ��Netflix API����û��ʲô��ͬ��ֻ��ʹ��Spring Boot�����ļ�������ѡ�������CommonClientConfigKey���������ں��Ĳ��֣�����Ϊ��̬�ֶν��м�顣

Spring Cloud��������ͨ��ʹ��@RibbonClient�����������ã�λ��RibbonClientConfiguration֮�ϣ�����ȫ���ƿͻ��ˡ�����

@Configuration
@RibbonClient(name = "foo", configuration = FooConfiguration.class)
public class TestConfiguration {
}
����������£��ͻ�����RibbonClientConfiguration���Ѿ����ڵ������FooConfiguration�е��κ������ɣ�����ͨ���Ḳ��ǰ�ߣ���

####����
FooConfiguration������@Configuration������ע�⣬��������Ӧ�ó��������ĵ�@ComponentScan�У�����������@RibbonClients���������ʹ��@ComponentScan����@SpringBootApplication��������Ҫ��ȡ��ʩ������������罫�����һ�������ģ����ص��İ��У�����ָ��Ҫ��@ComponentScan����
Spring Cloud NetflixĬ�������ΪRibbon��BeanType beanName��ClassName���ṩ����bean��

IClientConfig ribbonClientConfig��DefaultClientConfigImpl

IRule ribbonRule��ZoneAvoidanceRule

IPing ribbonPing��NoOpPing

ServerList<Server> ribbonServerList��ConfigurationBasedServerList

ServerListFilter<Server> ribbonServerListFilter��ZonePreferenceServerListFilter

ILoadBalancer ribbonLoadBalancer��ZoneAwareLoadBalancer

ServerListUpdater ribbonServerListUpdater��PollingServerListUpdater

����һ�����͵�bean�����������@RibbonClient���ã����������FooConfiguration���У�������������������ÿ��bean������

@Configuration
public class FooConfiguration {
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
}
����PingUrl����NoOpPing��

ʹ�������Զ���Ribbon�ͻ���
�Ӱ汾1.2.0��ʼ��Spring Cloud Netflix����֧��ʹ��������Ribbon�ĵ��������Զ���Ribbon�ͻ��ˡ�

���������ڲ�ͬ�����и�������ʱ����Ϊ��

֧�ֵ�����������ʾ��Ӧ��<clientName>.ribbon.Ϊǰ׺��

NFLoadBalancerClassName��ӦʵʩILoadBalancer

NFLoadBalancerRuleClassName��ӦʵʩIRule

NFLoadBalancerPingClassName��ӦʵʩIPing

NIWSServerListClassName��ӦʵʩServerList

NIWSServerListFilterClassNameӦʵʩServerListFilter

ע��
����Щ�����ж������������ʹ��@RibbonClient(configuration=MyRibbonConfig.class)�����bean����Spring Cloud Netflix�ṩ��Ĭ��ֵ��
Ҫ���÷�������users��IRule�������������������ݣ�

application.yml
users:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule
�й�Ribbon�ṩ��ʵ�֣������Ribbon�ĵ���

####��Eureka��ʹ��Ribbon
��Eureka��Ribbon���ʹ�ã������߶�����·���ϣ�ʱ��ribbonServerList������չΪDiscoveryEnabledNIWSServerList����չ��ΪEureka�ķ������б�������NIWSDiscoveryPing�滻IPing�ӿڣ�����Eureka��ȷ���������Ƿ�������Ĭ������°�װ��ServerList��һ��DomainExtractingServerList����Ŀ����ʹ����Ԫ���ݿ����ڸ���ƽ����������ʹ��AWS AMIԪ���ݣ�����Netflix�����ģ���Ĭ������£��������б�ʹ��ʵ��Ԫ���ݣ���Զ�̿ͻ��˼���eureka.instance.metadataMap.zone�����ṩ�ġ�������Ϣ���������ȱ�٣������ʹ�÷������������е�������Ϊ��������������������˱�־approximateZoneFromHostname����һ��������Ϣ���ã���������ServerListFilter��ʹ�á�Ĭ������£��������ڶ�λ��ͻ�����ͬ����ķ���������ΪĬ��ֵΪZonePreferenceServerListFilter��Ĭ������£��ͻ��˵�������Զ��ʵ���ķ�ʽ��ͬ����ͨ��eureka.instance.metadataMap.zone��

ע��
���ÿͻ����������ͳ��archaius����ʽ��ͨ��һ����Ϊ��@zone�����������ԣ�������ã�Spring Cloud������ʹ�������������ã���ע�⣬�ü����뱻���ã���YAML�����У���
ע��
���û����������������Դ������ڿͻ������ã���ʵ�������෴�����в²⡣���ǽ�eureka.client.availabilityZones������������ӳ�䵽�����б�������ʵ���Լ�������ĵ�һ�����򣨼�eureka.client.region����Ĭ��Ϊ��us-east-1��Ϊ�뱾��Netflix�ļ����ԣ���
ʾ�������ʹ��Ribbon��ʹ��Eureka
Eureka��һ�ַ���ķ�ʽ������Զ�̷������ķ��֣�����������ڿͻ����ж���URL����Ӳ���룬�����������ʹ������Ribbon��Feign��Ȼ�����õġ��������Ѿ�Ϊ���̵ꡱ������@RibbonClient������Eurekaδ��ʹ�ã�����������·���ϣ���Ribbon�ͻ���Ĭ��Ϊ�����õķ������б��������ṩ����������

application.yml
stores:
  ribbon:
    listOfServers: example.com,google.com
####ʾ������Ribbon�н���Eurekaʹ��
��������ribbon.eureka.enabled = false����ȷ������Ribbon��ʹ��Eureka��

application.yml
ribbon:
  eureka:
   enabled: false
ֱ��ʹ��Ribbon API
��Ҳ����ֱ��ʹ��LoadBalancerClient������

public class MyClass {
    @Autowired
    private LoadBalancerClient loadBalancer;

    public void doStuff() {
        ServiceInstance instance = loadBalancer.choose("stores");
        URI storesUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
        // ... do something with the URI
    }
}
����Ribbon����
ÿ��Ribbon�����Ŀͻ��˶���һ����Ӧ����Ӧ�ó��������ģ�Spring Cloudά�������Ӧ�ó����������ڵ�һ�������б��ӳټ��ص������Ŀͻ��ˡ�����ͨ��ָ��Ribbon�ͻ��˵����ƣ�������ʱ�����Ը��Ĵ��ӳټ�����Ϊ���Ӷ����м�����Щ��Ӧ�ó��������ġ�

application.yml<br>
ribbon:<br>
  &nbsp;&nbsp;&nbsp;&nbsp;eager-load:<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;enabled: true<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;clients: client1, client2, client3<br>