# STRO.MY tools

A set of two simple tools commissioned by Jiří Trtík for his composition Concert for Forest.

**TypeMidi** records times of pressed keys and converts them to musical rhythms. Output formats are musicxml and midi. Musicxml output is quantized. Midi is written precisely and can be converted to sheet music by importing to e.g. MuseScore. SMPTE is a midi sub-format that has an influence on the import process, but it is less compatible (does not work with e.g. Sibelius) than non-SMPTE midi.

**MidiFreq** plays a chord based on arbitrary frequencies. There are two main options: Tempered plays the closest notes in equal temperament, Exact plays the given frequencies exactly. Save button saves the chord as midi. A frequency can be disabled by commenting it out with a # (hashtag) sign.
