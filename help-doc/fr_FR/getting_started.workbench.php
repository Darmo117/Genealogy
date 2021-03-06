<?php
ob_start();
?>
  <p>
    Lorsque le logiciel est démarré, la première chose que vous voyez est une zone vide. C'est ici
    que vous éditerez votre arbre généalogique après l'avoir créé. Vous pouvez créer un arbre en
    sélectionnant <span class="menu">Fichier &gt; <img src="../images/new_tree.png"> Nouvel arbre…</span>
    ou en cliquant sur <span class="button"><img src="../images/new_tree_32.png"> Nouvel arbre…</span>
    ou encore en appuyant sur <span class="key-stroke">Ctrl+N</span>.
    Il vous sera demandé d'entrer un nom pour l'arbre. Une fois cela fait, cliquez sur
    <span class="button">Valider</span>.
  </p>
  <p>
    Une fois le projet créé, vous devriez voir que certains boutons sont maintenant accessibles.
    Nous pouvons alors commencer à travailler.
  </p>
  <p class="next-topic">Prochain sujet&#8239;: <a href="getting_started.cards.php">Fiches</a></p>
<?php
$content = ob_get_clean();
$title = 'L\'Espace de travail';
$lang = 'fr_FR';
require_once '../template.php';