<?php
$autoload = __FILE__;
$autoload = str_replace(basename(__FILE__) , "", $autoload);
require_once $autoload . '../../../autoload.php';

$plugin = new SBTKBookmarks();
$plugin->createHeader();
$plugin->createBookmarkGrid(array());


