/**
 * Created by Chaly on 16/5/27.
 */
require(
    [
        "jquery", "backbone", "Handlebars",
        // "migrate", "slideto", "wiggle", "ba-bbq", "highlight", "highlight_ext", "jsoneditor",
        // "lodash", "marked", "swagger-ui"
        "swagger-ui/swagger-ui.js"
    ],
    function ($, Backbone,Handlebars) {
        Backbone.View = (function (View) {
            return View.extend({
                constructor: function (options) {
                    this.options = options || {};
                    View.apply(this, arguments);
                }
            });
        })(Backbone.View);
        var bPl = require("swagger-ui/swagger-ui.js");
        console.log("+++++++++++++++++++++++++++++++++>");
        console.log(Backbone);
    }
);