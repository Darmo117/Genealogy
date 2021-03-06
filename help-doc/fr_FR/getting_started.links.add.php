<?php
ob_start();
?>
  Pour ajouter un lien, suivez une de ces méthodes&#8239;:
  <ul>
    <li>aller à <span class="menu">Édition &gt; <img src="../images/add_link.png"> Ajouter un lien…</span>&#8239;;</li>
    <li>ou cliquer sur <span class="button"><img src="../images/add_link_32.png"> Ajouter un lien…</span>&#8239;;</li>
    <li>ou appuyer sur <span class="key-stroke">Ctrl+L</span></li>
  </ul>
  Maintenant, sélectionnez la première fiche à connecter, puis cliquez sur la seconde.
  Vous devriez voir cette fenêtre&#8239;:<br />
  <img class="center" src="../images/add_link_dialog-fr_FR.png"><br />
  Cette première case (1) permet d'indiquer si la relation est un mariage.<br />
  Les deux champs suivants (2) sont la date et le lieu de début de la relation. La date peut être
  incomplète.<br />
  La case dans la zone (3) permet d'indiquer si la relation a été rompue. Si elle est cochée, le
  champ en-dessous est dégrisé.<br />
  Les deux champs en-dessous (4) contiennent les noms du couple.<br />
  La zone (5) vous permet d'ajouter/enlever des enfants issus de la relation. Pour ajouter des
  enfants, sélectionnez les items dans la liste du bas puis cliquez sur le bouton
  <img src="../images/arrow_up.png">. Pour supprimer des enfants, sélectionnez les items de la
  liste du haut puis cliquez sur le bouton <img src="../images/arrow_down.png">. Vous pouvez
  rechercher une personne particulière grâce à la barre de recherche tout en bas.
  <p>
    Une fois que vous avez terminé, cliquez sur <span class="button">Valider</span> pour créer le
    lien. Si vous avez cliqué avant d'avoir terminé, pas de panique, vous pourrez toujours revenir
    éditer plus tard.
  </p>
  <p>
    Maintenant, il devrait y avoir un lien entre les deux fiches et les enfants.
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.links.edit.php">Éditer un lien</a></p>
<?php
$content = ob_get_clean();
$title = 'Ajouter un lien';
$lang = 'fr_FR';
require_once '../template.php';