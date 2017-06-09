# MyTodo [Android App | Uni Project]

<p>
This readme contains the most important information about the android application MyTodo and logs the implemented requirements.
</p>

<h2><b>General Information</b></h2>

<p>
This section contains general information about the software.
</p>
<table>
  <tr>
    <td>Software type</td>
    <td>Android Application</td>
  </tr>
  <tr>
    <td>Type</td>
    <td>Uni Project | single - no teamwork | subject: mobile application development</td>
   <tr>
    <td>Period</td>
    <td>summer semester 2017 | finished ...</td>
   <tr>
    <td>Function</td>
    <td>Manage your todos</td>
  </tr>
  <tr>
    <td>Development environment</td>
    <td>Android Studio</td>
  </tr>
   <tr>
    <td>Programming language</td>
    <td>Java</td>
  </tr>
  <tr>
    <td>Database</td>
    <td>SQLite</td>
  </tr>
</table>


<h2><b>Requirements</b></h2></ul>
<table>
  <tr>
    <th>Gruppe</th>
    <th>Id</th>
    <th>Anforderung</th>
    <th>Punkte</th>
    <th>Erledigt</th>
  </tr>
  <tr>
    <td>Datenmodell</td>
    <td>A</td>
    <td>Das Datenmodell für Todos soll es erlauben, die folgende Information zu repräsentieren:</td>
    <td>7</td>
    <td></td>
  </tr>
  <tr>
    <td></td>
    <td>1</td>
    <td>den Namen des Todo</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
   <tr>
    <td></td>
    <td>2</td>
    <td>eine Beschreibung des Todo</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
   <tr>
    <td></td>
    <td>3</td>
    <td>die Information darüber, ob das Todo erledigt wurde oder nicht</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>4</td>
    <td>die Information darüber, ob es sich um ein besonders wichtiges/ ‘favourite’ Todo handelt oder nicht</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>5</td>
    <td>das Fälligkeitsdatum des Todo und eine Uhrzeit</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>6</td>
    <td>Alle Informationsbestandteile, inklusive des Namens, sollen nach Erstellung eines Todo änderbar sein.</td>
    <td>2</td>
     <td></td>
  </tr>
    <tr>
    <td>Speichern von Todos</td>
    <td>B</td>
    <td>Das Speichern von Todos soll sowohl mittels einer externen Webanwendung, als auch mittels eines lokalen Datenspeichers erfolgen.  Die Webanwendung wird Ihnen als Java EE Web Application zur Verfügung gestellt. </td>
    <td>12</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>7</td>
    <td>Auf dem Endgerät sollen Todos in einer SQLite Datenbank gespeichert werden.</td>
    <td>4</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>8</td>
    <td>Wurde eine Schreiboperation auf der lokalen SQLite Datenbank erfolgreich ausgeführt, soll die betreffende Operation auf der Webanwendung aufgerufen werden. Die durch die SQLite Datenbank zugewiesenen IDs können durch die Webanwendung übernommen werden</td>
    <td>3</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>9</td>
    <td>Ist beim Start der Anwendung kein Zugriff auf die Webanwendung möglich, soll eine Warnmeldung ausgegeben werden. In diesem Fall wird bis zum Ende der Anwendungsnutzung nur die lokale Datenbank verwendet.Die Fälle, dass eine initiale Verbindung während der App-Nutzung abbricht bzw.dass eine anfänglich nicht verfügbare Webanwendung während der Nutzung verfügbar wird, brauchen nicht berücksichtigt zu werden.</td>
    <td>1</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>10</td>
    <td>Ist die Webanwendung beim Start der Android Anwendung verfügbar, soll der folgende "Abgleich" implementiert werden:
- liegen lokale Todos vor, dann werden alle Todos auf Seiten der Web Applikation gelöscht und die lokalen Todos an die Web Applikation übertragen. - liegen keine lokalen Todos vor, dann werden alle Todos von der Web Applikation auf die lokale Datenbank übertragen.</td>
    <td>4</td>
     <td></td>
  </tr>
    <tr>
    <th>Anmeldung</th>
    <th>C</th>
    <th></th>
    <th>20</th>
     <th></th>
  </tr>
    <tr>
    <td></td>
    <td>11</td>
    <td>Die Anmeldung soll durch Eingabe einer Email und eines Passworts erfolgen und durch Betätigung eines Login Buttonsausgelöst     werden</td>
    <td>3</td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>12</td>
    <td>In das Eingabefeld für Email sollen nur Emailadressen eingegeben werden.</td>
    <td>1</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>13</td>
    <td>Wird keine Email-Adresse eingegeben,wird eine dauerhaft sichtbare Fehlermeldung ausgegeben.</td>
    <td>1</td>
     <td></td>
  </tr>
  <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
</table>
