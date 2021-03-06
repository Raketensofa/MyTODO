# MyTodo [Android App | Uni Project (Master)]

<h2><b>General Information</b></h2>

<p>
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
    <td>summer semester 2017 | started: 21.04.2017 - finished 06.07.2017</td>
   <tr>
    <td>Result</td>
    <td>98% - grade: 1.0 </td>
  </tr>
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
    <td>SQLite and Java EE Webservice</td>
  </tr>
</table>

<h2><b>Screenshots</b></h2></ul>
<p align="center">
  <img src="https://github.com/carolingellner/MyTODO/blob/master/Screenshot_20180807-201603.png" width="250" title="Startseite">
  <img src="https://github.com/carolingellner/MyTODO/blob/master/Screenshot_20180807-201628.png" width="250" title="Neues Todo anlegen">
  <img src="https://github.com/carolingellner/MyTODO/blob/master/Screenshot_20180807-201655.png" width="250" title="Todo Detail">
</p>

<h2><b>Requirements</b></h2></ul>
<table>
  <tr>
    <th>Gruppe</th>
    <th>Id</th>
    <th>Funktionale Anforderung</th>
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
     <td>&times;</td>
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
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>8</td>
    <td>Wurde eine Schreiboperation auf der lokalen SQLite Datenbank erfolgreich ausgeführt, soll die betreffende Operation auf der Webanwendung aufgerufen werden. Die durch die SQLite Datenbank zugewiesenen IDs können durch die Webanwendung übernommen werden</td>
    <td>3</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>9</td>
    <td>Ist beim Start der Anwendung kein Zugriff auf die Webanwendung möglich, soll eine Warnmeldung ausgegeben werden. In diesem Fall wird bis zum Ende der Anwendungsnutzung nur die lokale Datenbank verwendet.Die Fälle, dass eine initiale Verbindung während der App-Nutzung abbricht bzw.dass eine anfänglich nicht verfügbare Webanwendung während der Nutzung verfügbar wird, brauchen nicht berücksichtigt zu werden.</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>10</td>
    <td>Ist die Webanwendung beim Start der Android Anwendung verfügbar, soll der folgende "Abgleich" implementiert werden:
- liegen lokale Todos vor, dann werden alle Todos auf Seiten der Web Applikation gelöscht und die lokalen Todos an die Web Applikation übertragen. - liegen keine lokalen Todos vor, dann werden alle Todos von der Web Applikation auf die lokale Datenbank übertragen.</td>
    <td>4</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td>Anmeldung</td>
    <td>C</td>
    <td></td>
    <td>20</td>
     <td></td>
  </tr>
    <tr>
    <td></td>
    <td>11</td>
    <td>Die Anmeldung soll durch Eingabe einer Email und eines Passworts erfolgen und durch Betätigung eines Login Buttonsausgelöst     werden</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>12</td>
    <td>In das Eingabefeld für Email sollen nur Emailadressen eingegeben werden.</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>13</td>
    <td>Wird keine Email-Adresse eingegeben,wird eine dauerhaft sichtbare Fehlermeldung ausgegeben.</td>
    <td>1</td>
     <td>&times;</td>
  </tr>
  <tr>
    <td></td>
    <td>14</td>
    <td>Wird die Eingabe des Email Feldes geändert, verschwindet die Fehlermeldung unmittelbar bei Eingabe/Löschen eines Zeichens..</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>15</td>
    <td>Passwörter sollen numerisch und genau 6 Ziffern lang sein</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>16</td>
    <td>Die Eingabe soll verschleiert (‘ausgepunktet’) werden.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>17</td>
    <td>Die Betätigung des Login Buttons soll nur möglich sein, wenn Werte für Email und Passwort eingegeben wurden.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>18</td>
    <td>Nach Betätigung des Login Buttons sollen die eingegebenen Werte an einen Server übermittelt und dort überprüft werden</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>19</td>
    <td>Die Überprüfung soll asynchron erfolgen</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>20</td>
    <td>Solange die Überprüfung läuft, soll ein ProgressDialog eingeblendet werden.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>21</td>
    <td>Schlägt die Überprüfung fehl, wird eine dauerhaft sichtbare Fehlermeldung ausgegeben.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>22</td>
    <td>Wird eines der beiden Felder geändert, verschwindet die Fehlermeldung unmittelbar bei Eingabe/Löschen eines Zeichens</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>23</td>
    <td>Bei erfolgreicher Überprüfung der eingegebenen Werte soll die Anzeige der Todos erfolgen.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>24</td>
    <td>Besteht beim Start der Android-Anwendung keine Verbindung zur Webanwendung, wird sofort die Todoliste angezeigt. Eine lokale Anmeldung ist nicht erforderlich. (Anm.: die Vergabe von Punkten für diese Anforderung erfolgt nur, wenn die Anmeldung unter Verwendung der Webanwendung grundsätzlich umgesetzt ist.)</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td>Todoliste</td>
    <td>D</td>
    <td></td>
    <td>20</td>
    <td></td>
  </tr>
  <tr>
    <td></td>
    <td>25</td>
    <td>Die Anzeige der Todoliste soll eine Übersicht über alle Todos darstellen und die Erstellung neuer Todos ermöglichen</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td></td>
    <td>Sie soll für jedes Todo die folgende Information darstellen:</td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>26</td>
    <td>den Namen</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>27</td>
    <td>das Fälligkeitsdatum</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>28</td>
    <td>das Erledigsein/Nicht-Erledigtsein</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>29</td>
    <td>die Wichtigkeit</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>30</td>
    <td>Sie soll es dem Nutzer außerdem ermöglichen, sich für jedes Todo dessen Details anzeigen zu lassen.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>31</td>
    <td>Änderungen der Todoliste, die in der Detailansicht eines Todos getätigt werden können, sollen bei Rückkehr in der Übersicht angezeigt werden.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>32</td>
    <td>Die Information zum Erledigtsein/Nicht-Erledigtsein bzw. zur Wichtigkeit soll modifiziert werden können, ohne die Detailanzeige anzufordern</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>33</td>
    <td>Todos sollen grundsätzlich nach Erledigt/Nichterledigt sortiert sein und dann wahlweise nach Wichtigkeit+Datum oder nach Datum+Wichtigkeit.</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>34</td>
    <td>Dem Nutzer soll es möglich sein, die Anzeige nach Datum+Wichtigkeit vs. Wichtigkeit+Datum über ein Optionsmenü bzw. Action Bar Optionen auszuwählen.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>35</td>
    <td>Überfällige Todos – d.h. Todos mit abgelaufenem Fälligkeitsdatum – sollen visuell besonders hervorgehoben werden.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td>Detailansicht</td>
    <td>E</td>
    <td></td>
    <td>15</td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>36</td>
    <td>Die Detailansicht soll alle durch ein Todo repräsentierten Daten darstellen.</td>
    <td>4</td>
    <td>&times;</td>
  </tr>
   <tr>
    <td></td>
    <td></td>
    <td>Sie soll außerdem die Änderung zumindest der folgenden Daten eines Todo ermöglichen:</td>
    <td></td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>37</td>
    <td>Name</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>38</td>
    <td>Beschreibung</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
      <tr>
    <td></td>
    <td>39</td>
    <td>Fälligkeitsdatum und Uhrzeit</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
      <tr>
    <td></td>
    <td>40</td>
    <td>Erledigtsein</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
      <tr>
    <td></td>
    <td>41</td>
    <td>Für die Einstellung von Datum und Uhrzeit sollen die für diesen Zweck durch Android bereitgestellten UI Bedienelemente verwendet werden.</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>42</td>
    <td>Das Löschen eines Todos soll ebenfalls über die Detailansicht ermöglicht und vor Durchführung rückbestätigt werden.</td>
    <td>3</td>
    <td>&times;</td>
  </tr>
   <tr>
    <td>Verknüpfung mit Kontakten</td>
    <td>F</td>
    <td></td>
    <td>10</td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>43</td>
    <td>Erlauben Sie auf Ebene des Datenmodells die Assoziation eines Todo mit einer Menge von Kontakten.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
  <tr>
    <td></td>
    <td>44</td>
    <td>Erlauben Sie dem Nutzer, auf Ebene der Detailansicht Todos optional mit einer Liste von Kontakten zu verknüpfen.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>45</td>
    <td>Die Auswahl der Kontakte soll auf Grundlage einer Darstellung aller verfügbaren Kontakte erfolgen.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>46</td>
    <td>Zeigen Sie die verknüpften Kontakte in der Detailansicht für Todos an.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>47</td>
    <td>Ermöglichen Sie das Entfernen von Kontakten zur Liste der verknüpften Kontakte eines Todo</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>48</td>
    <td>Ermöglichen Sie außerdem, dass dem Nutzer für jeden Kontakt die Möglichkeit der Kontaktaufnahme per Mail oder SMS gegeben wird, falls eine Mailadresse oder Mobilfunknummer vorhanden sind.</td>
    <td>2</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td></td>
    <td>49</td>
    <td>Bei Kontaktaufnahme sollen die Mailadresse/Mobilfunknummer, der Titel und die Beschreibung des Todos der jeweils verwendeten Android App übermittelt werden.</td>
    <td>1</td>
    <td>&times;</td>
  </tr>
    <tr>
    <td>Dokumentation</td>
    <td>G</td>
    <td>Erstellen Sie eine Dokumentation Ihrer Anwendung als UML Klassendiagramm</td>
    <td>6</td>
    <td></td>
  </tr>
    <tr>
    <td></td>
    <td>50</td>
    <td>Die architektonisch relevanten Klassen der Anwendung sollen hinsichtlich ihrer relevanten Attribute und Methoden dokumentiert und zueinander in Beziehung gesetzt werden.</td>
    <td>6</td>
    <td>&times;</td>
  </tr>
  
</table>

