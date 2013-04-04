package cantroserver.networking;

public enum Commands {


	//Commands from Client TO Server
	INPUTUPDATE(0),
	/* INPUTUPDATE PROTOCOL
	 * COMMAND:
	 * PLAYER UID:
	 * CHAR:
	 * 		For which this char is taken from an int derived from the input commands from the client such that
	 * 		for the first 8 bits in said int,
	 * 			0		0		0		0		0		0		0		0
	 * 									Shoot	Up		Down	Left	Right
	 *		the int then gets 40 added to it to prevent a blank char in the lower ranges of ASCII
	 */
	POSUPDATE(1),
	SPAWNBULLET(2),
	PING(3),
	USERNAMEAPPLICATION(4),
    ADDUSERTOLIST(5),
    REMOVEUSERFROMLIST(6),

	//Commands from Server TO Client
	DAMAGE(7),
	KICK(8),
	BAN(9),
	USERNAMEAPPLICATIONREJECTED(10),
	USERNAMEAPPLICATIONACCEPTED(11);

	public int val;

	Commands(int i)
	{
		val = i;
	}

}
