<?php

# аргументы s
$API_KEY = $argv[1];
$WEBHOOK = $argv[2];
$userEmail = $argv[3];
$taskId = $argv[4];
$siteUrl = $argv[5];
$comment = $argv[6];

$comment = trim($comment);
$message = "{$comment}.";
if (!empty($siteUrl)) {
    $message .= "\n\nСсылка на сайт для просмотра: $siteUrl";
}

$url = $WEBHOOK;
$data = array(
    'API_KEY' => $API_KEY,
    'teamcityTaskId' => $taskId,
    'teamcityUserEmail' => $userEmail,
    'teamcityMessage' => $message
);

$options = array(
    CURLOPT_URL => $url,
    CURLOPT_POST => true,
    CURLOPT_POSTFIELDS => json_encode($data),
    CURLOPT_HTTPHEADER => array('Content-Type: application/json'),
    CURLOPT_RETURNTRANSFER => true
);

$ch = curl_init();
curl_setopt_array($ch, $options);
$result = curl_exec($ch);

if (curl_errno($ch)) {
    echo 'Error:' . curl_error($ch);
}

curl_close($ch);
?>
