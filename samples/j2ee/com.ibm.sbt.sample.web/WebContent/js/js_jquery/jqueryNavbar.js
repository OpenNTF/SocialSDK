(function($) {

    $.fn.showSnippetDivContents = function(){
        this.css("display", ""); 
        this.siblings().css("display", "none");
        return this;
    };
    
    /**
     * Adds a CodeMirror Instance to the specified object.
     */
    $.fn.addCodeMirror = function(mode){
        var $code = this.html(),
        $unescaped = $code.indexOf("&lt") != -1 ? $('<div/>').html($code).text().trim() : $code.trim();
        
        this.empty();
        CodeMirror(this.get(0), {
            value: $unescaped,
            matchBrackets: true,
            mode: mode,
            lineNumbers: true
        });
        return this;
    };
    
    /**
     * Removes the CodeMirror instance from the specified object (if present).
     */
    $.fn.removeCodeMirror = function(){
        var el = this.get(0);
        if(el && el.firstChild && el.firstChild.CodeMirror){
            var html = el.firstChild.CodeMirror.getValue();
            this.empty();
            this.html(html);
        }
    },
    
    /**
     * Removes CodeMirrors from other divs and adds one to the specified div (if not present)
     */
    $.fn.setCodeMirror = function(mode){
        this.siblings().each(function(){
            $(this).removeCodeMirror();
        });
        var el = this.get(0);
        if(el && !el.firstChild.CodeMirror){
            this.addCodeMirror(mode);
        }
        
        return this;
    };
    
    $("#snippetContainer").ajaxComplete(function(){
        showNavContent($(".span8 ul li.active"));
    });
    
    /**
     * Display the correct div based on the nav li that was just pressed. Add CodeMirror if not present, remove the other divs' CodeMirrors.
     */
    function showNavContent($li){
        switch($li.text().trim().toUpperCase()){
        case "JAVASCRIPT": 
            var jsDiv = $("#jsContents");
            jsDiv.showSnippetDivContents();
            jsDiv.setCodeMirror("text/javascript");
            break;
        case "HTML": 
            var htmlDiv = $("#htmlContents");
            htmlDiv.showSnippetDivContents();
            htmlDiv.setCodeMirror("text/html");
            break;
        case "CSS": 
            var cssDiv = $("#cssContents");
            cssDiv.showSnippetDivContents(); 
            cssDiv.setCodeMirror("text/css"); 
            break;
        case "DOCUMENTATION": 
            var docDiv = $("#docContents");
            docDiv.showSnippetDivContents(); 
            docDiv.setCodeMirror("text/plain"); 
            break;
        default: 
            console.log($li.text().trim().toUpperCase());
            break;
        }
            
        $li.siblings().removeClass('active');
        $li.addClass('active');
    };
    
    $(document).ready(function(){
        $(".span8 ul li a").click(function(e){
            e.preventDefault();
            
            var $li = $(this).parent();
            showNavContent($li);
        });
      
    });
    
})(jQuery);