$(document).ready(function () {
    //nav
    $("#mnavh").click(function () {
        $("#mnavh").toggleClass("open");
    });
    //search  
    $(".searchico").click(function () {
        $('.searchbox').toggleClass("open");
        $(".search").toggleClass("open");
    });
    //searchclose 
    $(".searchclose").click(function () {
        $(".search").removeClass("open");
        $('.searchbox').removeClass("open");
    });
    //banner
    $('#banner').easyFader();
    //nav menu   
    $(".menu").click(function (event) {
        $(this).children('.sub').slideToggle();
    });
    //tab
    $('.tab_buttons li').click(function () {
        $(this).addClass('newscurrent').siblings().removeClass('newscurrent');
        $('.newstab > div:eq(' + $(this).index() + ')').css('display', 'flex').siblings().hide();
    });
});