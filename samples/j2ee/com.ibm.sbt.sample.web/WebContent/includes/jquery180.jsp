<script type="text/javascript" src="/sbt.jquery182/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/sbt/js/libs/require.js"></script>

<script type="text/javascript">
requirejs.config({
       paths: {
           'has' : '/sbt/js/libs/has',
           'jquery' : '/sbt.jquery182/js/jquery-1.8.0.min',
           'jquery/ui' : '/sbt.jquery182/js/jquery-ui-1.8.23.custom.min',
           'requirejs/i18n' : '/sbt/js/libs/requirejsPlugins/i18n',
           'requirejs/text' : '/sbt/js/libs/requirejsPlugins/text'
        },
        shim : {
            'jquery/ui' : {
                deps : [ 'jquery' ],
                exports : '$'
            }
        }
    });
</script>

<link rel="stylesheet" type="text/css" title="Style" href="/sbt.jquery182/css/ui-lightness/jquery-ui-1.8.23.custom.css">
