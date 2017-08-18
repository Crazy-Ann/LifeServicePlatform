$(function(){
	$(".btn-sos").click(function(){
		$(".mask,.showdiv").show();
	});
	$(".mask,.btn-cancel").click(function(){
		$(".mask,.showdiv").hide();
	});
})