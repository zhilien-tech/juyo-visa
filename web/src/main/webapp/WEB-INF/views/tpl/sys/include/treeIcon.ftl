<script id="tree_icon" type="text/x-kendo-template">
    #if(data.icon){#
    <i class='tree-icon fa-#:data.icon#' style='color:#:data.color#'></i>#:data.name#
    #}else{#
    <i class='tree-file'></i>#:data.name#
    #}#
</script>