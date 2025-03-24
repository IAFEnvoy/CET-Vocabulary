# CET Vocabulary

This mod can let you learn English in game.

## Usage

After join a world, there will be a countdown in the bottom left corner.

<div align=center><img src="https://raw.githubusercontent.com/IAFEnvoy/CET-Vocabulary/refs/heads/master/img/1.webp" style="width:400px;text-align:center;" alt=""></img></div>

When the countdown to 0, it will open a quiz screen.

<div align=center><img src="https://raw.githubusercontent.com/IAFEnvoy/CET-Vocabulary/refs/heads/master/img/2.webp" style="width:400px;text-align:center;" alt=""></img></div>

If you don't answer correctly, the remain chances will reduce. When it reduced to 0, the game will automatically close.

## Configuration

```json5
{
  //Enable this mod
  "enable": true,
  //Interval between quiz, in ticks
  "interval": 3600,
  //How many wrong answers before close
  "maxFailureChance": 10,
  //Whether it need to render time
  "renderTime": true,
  //Up to 9
  "choiceCount": 6,
  //Allowed: CET4, CET6, BOTH
  "wordCollection": "CET4"
}
```