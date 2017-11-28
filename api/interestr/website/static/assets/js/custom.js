var transparentDemo = true;
var fixedTop = false;

$(window).scroll(function(e) {
    oVal = ($(window).scrollTop() / 170);
    $(".blur").css("opacity", oVal);
});

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip({
        container: 'body'
    });
});
