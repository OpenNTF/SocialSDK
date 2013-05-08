<script type="text/javascript" src="/sbt/js/libs/require.js"></script>

<script type="text/javascript">
requirejs.config({
       paths: {
           'jquery' : '//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery',
           'jquery/ui' : '//ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui',
        },
        shim : {
            'jquery/ui' : {
                deps : [ 'jquery' ],
                exports : '$'
            }
        }
    });
</script>

<link rel="stylesheet" type="text/css" title="Style" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/base/jquery-ui.css">
