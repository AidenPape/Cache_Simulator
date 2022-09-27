# Spell-Checker

This program uses a dictionary of words to check through text files and see if those words are within the dictionary, if they arent they are considered 
misspelled and the user have a few options of what to do. Ignore, which says the user does not what to change this word here or or any future occurrences.
Replace, which then calls the suggestion method and gives the user suggested words that they can decide to replace with. Or they can select next, with is 
like ignore but future occurrences of the word will still be considered misspelled. The code uses a hash table for the dictionary to search for word 
quicky, it also prints a corrected text file and a text file with the misspelled words and the line there were on. It uses bucketsort to sort the arraylists
in the misspelled array and then prints all the misspelled words in lexicographic order.
