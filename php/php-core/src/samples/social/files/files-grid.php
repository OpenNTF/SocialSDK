<?php

$autoload = __FILE__;
$autoload = str_replace(basename(__FILE__) , "", $autoload);
require_once $autoload . '../../../autoload.php';

$plugin = new SBTKFiles();
$plugin->createHeader();

// Create a file grid
$plugin->createCompactFilesGrid(array());



