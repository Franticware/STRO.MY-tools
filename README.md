#STRO.MY tools

A set of two simple tools commissioned by [Jiří Trtík](https://jiritrtik.com/) for his composition Concert for Forest.

**TypeMidi** records times of pressed keys when writing text into a text field.
Then it converts them to musical rhythms. Output formats are musicxml and midi.
Musicxml output is quantized.
Midi is written precisely and can be converted to sheet music by importing to e.g. MuseScore.
Midi output can be optionally [SMPTE](https://en.wikipedia.org/wiki/SMPTE_timecode)-timed.
SMPTE is a midi sub-format that influences the import process and subsequently the resulting music, but it is less compatible (e.g. does not work with Sibelius) than non-SMPTE midi.
It is recommended to experiment with both variants to find which one gives better output for a particular input.

**MidiFreq** plays a chord based on arbitrary frequencies.
There are two main options: Tempered plays the closest notes in equal temperament, Exact plays the given frequencies exactly.
Save button saves the chord as midi.
A frequency can be disabled by commenting it out with a # (hashtag) sign.
