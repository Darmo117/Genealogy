<?php
ob_start();
?>
  <p>
    The following topics provide help on how to use the Workbench to create family trees. They also
    explain customization options.
  </p>
  <h2>Workbench</h2>
  <p>
    The term Workbench refers to the main editing interface. It is composed of the editing zone,
    the tool bar above it and the menu bar at the top of the window.
  </p>
  <p class="next-topic">Next topic: <a href="getting_started.php">Getting started</a></p>
<?php
$content = ob_get_clean();
$title = 'Overview';
require_once '../template.php';