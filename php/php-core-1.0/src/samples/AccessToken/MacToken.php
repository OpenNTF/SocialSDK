<?php

namespace Fishtrap\Guzzle\Plugin\AccessToken;

class MacToken implements TokenInterface
{
    public function __construct($config)
    {
        $this->config = $config;
    }

    public function getFormat()
    {
        return 'MAC';
    }

    public function __toString()
    {
        $macString = sprintf('%s ', $this->getFormat());
        foreach ($this->config as $key => $value) {
            $macString .= sprintf('%s="%s",'.PHP_EOL, $key, $value);
        }
        return trim($macString, PHP_EOL.",");
    }

    public function setFormat($format)
    {
        $this->format = $format;
    }
}
