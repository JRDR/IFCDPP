$(document).ready(function(){
    $(document).on('click', '.page-button', function (e) {
        let categoryId = $(".current-category").val();
        let page = $(this).val();
        location.href = "/catalog?categoryId=" + categoryId + "&page=" + page;});
});