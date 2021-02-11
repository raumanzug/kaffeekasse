# `kaffeekasse` - a system for managing coffee supply in business organizations

This software is made for teams in business organizations for keeping
track about coffee supply.  `kaffeekasse` supposes that a team member
serves as accountant who uses this software.  Team mates delivers packs
of coffee into the stock.  The accountant will credit to him the advanced
price for this pack of coffee.  The accountant also accounts the number
of cups of coffee pulled by each team member.  At regular intervals a
settlement happens.  For each team mate the number of coffees will be
debited to its deposit.  `kaffeekasse` also can produce tally sheets.
They will be printed out and hanged out in the kitchen where team member
make a stroke for each pulled cup of coffee.

## Use cases.

### Use cases on participants.

* `kaffeekasse` can add a participant into the team;
* `kaffeekasse` can make inactive a participant;


### deposit related use cases.

* `kaffeekasse` can credit the advanced price for a pack of coffee to the participant who delivered it;
* `kaffeekasse` can account a number of cups for each participant who pulled them;
* `kaffeekasse` can change the price of a cup of coffee;
* `kaffeekasse` can manage settlements of a accounting period where number of cups will be debited;
* `kaffeekasse` can settle deposit by paying debts;
* `kaffeekasse` can store a tally sheet onto disk;


### accounting related use cases.

* `kaffeekasse` holds a deposit of a bank;
* `kaffeekasse` holds booking entries;
* `kaffeekasse` can print accounting periods;
* `kaffeekasse` can print out booking entries of current accounting period;


## How to get `kaffeekasse` up and running.

### Database setup.

Change your `pom.xml` maven's configuration file and your
`persistence.xml` persistence unit if you want something other.  We use
mysql, the database `kaffeekasse` and the user `kaffeekasse` which has
all rights on it and no password:

	CREATE USER kaffeekasse;
	CREATE DATABASE kaffeekasse;
	USE DATABASE kaffeekasse;
	GRANT ALL ON kaffeekasse TO kaffeekasse;

### Maven setup.

We use trisquel 9.0 Linux.  We hope you are able to adapt the following
instructions to your system:

* Install necessary packages (become root before it):

	apt-get install libargparse4j-java libeclipselink-java libitext5-java libjson-simple-java libmysql-java maven openjdk-8-jdk-headless

* Provide `settings.xml` configuration file (drop root privileges before it):

	cp /etc/maven/settings.xml ~/.m2/settings.xml

* Edit `~/.m2/settings.xml`:

	< settings >

	...

	   < profiles >

	...

	      < profile >

	         < id >kaffeekasser< /id >

	         < repositories >

	            < repository >

	               < id >trisquel< /id >

	               < name >local repo from trisquel apt package < /name >

	               < url >file:///usr/share/maven-repo< /url >

	            < /repository >

	         < /repositories >

	      < /profile >

	...

	   < /profiles >

	...

	   < activeProfiles >

	      < activeProfile >kaffeekasse< /activeProfile >

	   < /activeProfiles >

	...

	< /settings >


Now you can use `kaffeekasse.sh` shell script in the root of this project.


## How to use `kaffeekasse`.

`kaffeekasse` is a non-interactive command line tool.  The following functions are available:

### participant related usage.

*
	kaffeekasse addParticipant < name >

  adds a participant with display name `name` to this system.  You only add a participant with this display name only once.

*
	kaffeekasse inactivateParticipant < name >

  make a participant with display name `name` inactive.

*
	kaffeekasse listParticipants

  prints out active participants.


### deposit related usage.

*
	kaffeekasse in < name > < price / ct >

  credit the advanced price of a pack of coffee to participant with display name `name`.

*
	kaffeekasse out < name > < nr of cups >

  account a number of cup of coffee to participant with display name `name`.

*
	kaffeekasse setPrice < price / ct >

  set price for a cup of coffee.

*
	kaffeekasse getPrice

  prints out the current price of a cup of coffee.

*
	kaffeekasse clear

  ends up the current account period a start a new one.

*
	kaffeekasse pay < name > < amount / ct >

  settle debts by paying in.

*
	kaffeekasse storeTally < filename >

  stores a tally sheet onti disk.


### accounting releated usage.

*
	kaffeekasse getBankDeposit

  print out the deposit.

*
	kaffeekasse listEntries

  list accounting entries of current accounting period.

*
	kaffeekasse listPeriods

  list all accounting periods.


## Formal description.

A program state can be descibed as:

	(M_B, (M_t, C_t)_{t in T}, P)

* `P` valid price of cup of coffee;
* `T` set of participants;
* `M_B` bank's deposit;
* `M_t` deposit of participant `t`;
* `C_t` number of pulled cup of coffee from participant `t`.


### initializations.

initial values for newly added participant `t` is:

	M_t = 0
	C_t = 0

after start of `kaffeekasse` state will be initialized as:

	M_B =  0
	T   =  {}

### formal description of above mentioned actions:

* `coffeeIn(t, p)`

	M_t += p

* `coffeeOut(t, n)`

	C_t += n

* `pay(t, p)`

	M_B += p

	M_t += p

* `setPrice(p)`

	P =  p

* `clear`

	(M_t -=  C_t * P)_{t in T}

	(C_t =  0)_{t in T}

