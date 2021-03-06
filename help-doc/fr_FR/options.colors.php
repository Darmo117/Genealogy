<?php
ob_start();
?>
  <p>
    Ce menu vous permet de changer la couleur de la plupart des composants. Vous pouvez y accéder
    depuis <span class="menu">Options &gt; <img src="../images/color_wheel.png"> Couleurs</span>.
  </p>
  <p>
    Vous devriez voir une fenêtre avec un arbre et un bouton coloré. Pour changer une couleur,
    sélectionnez l'option désirée dans l'arbre puis cliquez sur le bouton pour choisir la
    nouvelle couleur. Une fois que vous avez fini, cliquez sur <span class="button">Valider</span>
    pour appliquer les modifications. Vous n'avez pas besoin de rdémarrer l'application car les
    changements sont appliqués immédiatement.
  </p>
<?php
$content = ob_get_clean();
$title = 'Couleurs';
$lang = 'fr_FR';
require_once '../template.php';