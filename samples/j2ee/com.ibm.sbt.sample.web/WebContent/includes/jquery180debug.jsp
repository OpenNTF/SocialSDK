<script type="text/javascript" src="/sbt/js/libs/require.js"></script>

<script type="text/javascript">
requirejs.config({
       paths: {
           'has' : '/sbt/js/libs/has',
           'jquery' : '/sbt.jquery180/development-bundle/jquery-1.8.0',
           'jquery/ui' : '/sbt.jquery182/js/jquery-ui-1.8.24.custom.min',
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
