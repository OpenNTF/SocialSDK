<script type="text/javascript" src="/sbt/js/libs/require.js"></script>
<script type="text/javascript" src="/sbt.jquery182/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
requirejs.config({
       paths: {
           'has' : '/sbt/js/libs/has',
           'jquery/ui' : '/sbtx.jquery182/js/jquery-ui-1.8.24.custom.min',
           'requirejs/i18n' : '/sbt/js/libs/requirejsPlugins/i18n',
           'requirejs/text' : '/sbt/js/libs/requirejsPlugins/text'
        },
        shim : {
            'jquery/ui' : {
                deps : [ 'jquery' ],
                exports : 'jQuery'
            }
        }
    });
</script>
