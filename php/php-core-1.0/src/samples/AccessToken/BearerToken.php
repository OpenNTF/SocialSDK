<?php
namespace Fishtrap\Guzzle\Plugin\AccessToken;

use Guzzle\Common\Collection;

class BearerToken implements TokenInterface
{
    private $format;

    public function __construct($config)
    {
        $config = Collection::fromConfig($config, array(
            'token_format' => 'Bearer',
        ), array(
            'token_format',
        ));
        $this->tokenString = $config['access_token'];
        $this->format = $config['token_format'];
    }

    public function __toString()
    {
        return sprintf('%s %s', $this->getFormat(), $this->tokenString);
    }

    public function getFormat()
    {
        return $this->format;
    }

    public function setFormat($format)
    {
        $this->format = $format;
    }
}
