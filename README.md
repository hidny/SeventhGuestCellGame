# SeventhGuestCellGame
(Ataxx is the actual game name btw)
AI that plays the seventh Guest Cell Game

Rules:
See:
https://en.wikipedia.org/wiki/Ataxx

How to run:
Run the main function of runners/bleedingEdge/*java

In one of them you get to play first, and in the other the AI plays first.
The AI is way way better than me and managed to beat the 7th guest AI.

Current status:
Even though there's still a lot of TODOs and opportunities for optimization,
I managed to create a decent AI and I'm going to shelve this project for now.

FUTURE:

When/if come back to this,
I should probably work on the TODOs in ComplexEvalIterDeepHashAlphaBeta.
It has the latest and best code, and some of it should be shared with the other 
alpha beta AIs by moving it to helper functions.
There's also some less high priority ideas in MainGame.java

For optimization purposes,
I should also maybe work on representing the board with 2 longs.
1 long for the binary flags for peg/no peg and 1 long for the binary flags of player1 peg vs player2 peg

I could also make it compete with other people Ataxx AI.
(with the UAI protocol, It's the Universal Chess protocol, but for Ataxx)


Wiki:
https://en.wikipedia.org/wiki/Ataxx

Another option is to be part of the AI community link is here:
https://www.ataxx.org/
