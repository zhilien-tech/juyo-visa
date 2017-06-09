<script id="tree_icon" type="text/x-kendo-template">
    #if(data.icon){#
    <i class='tree-icon fa-#:data.icon#' style='color:#:data.color#'></i>
    #}else{#
    <i class='tree-file'></i>
    #}#
    <span pid='#:data.id#'>#:data.name#</span>
</script>