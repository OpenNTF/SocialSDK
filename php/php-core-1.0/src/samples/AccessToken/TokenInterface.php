<?php

namespace Fishtrap\Guzzle\Plugin\AccessToken;

interface TokenInterface
{
    public function __toString();

    public function getFormat();

    public function setFormat($format);
}