$(function(){
    //菜单点击
    J_iframe
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        $("#subTitle").text($(this).text().trim())
        $("#J_iframe").attr('src',url);
        return false;
    });
});