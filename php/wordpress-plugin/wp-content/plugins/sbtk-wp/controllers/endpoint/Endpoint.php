<?php
interface Endpoint {
	public function request($requestURL, $callbackURL, $method);
}

interface ResultData {
	function getHeaders();
	function getBody();
	function getCode();
	function getMessage();
}

?>