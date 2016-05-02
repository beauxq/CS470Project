package PerformanceTesting;

import DataObjects.Post;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class PostBuilder
{
    private static final int wordsPerPost = 3000;
    private static final int tagsPerPost = 10;
    private static final String dictionary = 
            "C:\\Users\\mrsun_000\\Google Drive\\Programs\\CS470Project"
            + "\\src\\java\\PerformanceTesting\\wordlist.txt";
    private static final Random rand = new Random();
    private static TreeMap<Double, String> dictionaryWords;
    private static final String newLine = "<br>"; // use "\n" or "<br>"
    
    private static PostBuilder singleton = null;

    private PostBuilder() throws Exception
    {
        dictionaryWords = populateDictionary(dictionary);
    }

    public static PostBuilder GetPostBuilder() throws Exception
    {
        if (singleton == null)
        {
            singleton = new PostBuilder();
        }
        return singleton;
    }

    private static TreeMap<Double, String> populateDictionary(String dictionary)
            throws Exception
    {
        HashMap<Double, String> words = new HashMap<>();
        
        File f = new File(dictionary);
        if(!f.exists())
        {
            throw new Exception("Invalid path for dictionary");
        }
        
        Path path = Paths.get(dictionary);
        try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8.name()))
        {
            while (scanner.hasNextLine())
            {
                
                String line = scanner.nextLine();
                String word = line.split("\t")[0];
                Double freq = Double.valueOf(line.split("\t")[1]);
                words.put(freq, word);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        TreeMap<Double, String> sortedWords = new TreeMap<>(words);
        return sortedWords;
    }
    
    public Post GetNewPost()
    {
        Post post = new Post();
        post.tags = CreateTags();
        post.pText = CreatePost(post.tags);
        post.pTitle = CreateTitle(post.pText);
        post.aName = GetName(post.pTitle);
        post.pDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());   
        return post;
    }

    public List<String> CreateTags()
    {
        List<String> tags = new ArrayList();
        for (int i = 0; i < tagsPerPost; i++)
        {
            String tag = randomWord(true);
            if (tags.contains(tag))
            {
                i--;
                continue;
            }
            tags.add(tag);
        }
        return tags;
    }

    public String CreatePost(List<String> tags)
    {
        String postContent = "";
        for (int i = 0; i < (wordsPerPost / 150); i++)
        {
            postContent += createParagraph(tags);
        }
        return postContent;
    }

    private String createParagraph(List<String> tags)
    {
        //avg paragraph length = 10 sentences (150 words)
        //min 3 sentences, max 17 sentences
        String paragraph = "";
        for (int i = 0; i < rand.nextInt(14) + 3; i++)
        {
            paragraph += createSentence(tags);
        }
        return paragraph + newLine + newLine;
    }

    private String createSentence(List<String> tags)
    {
        //avg sentence length = 15 words, min = 8, max = 22
        String sentence = "";
        sentence += toTitleCase(randomWord(false)) + " ";
        for (int i = 0; i < rand.nextInt(8) + 2; i++)
        {
            sentence += (rand.nextDouble() < .1 
                    ? randomTag(tags) : randomWord(false)) + " ";
        }
        sentence += randomWord(false) + randomPunctuation(false) + " ";
        for (int i = 0; i < rand.nextInt(8) + 3; i++)
        {
            sentence += (rand.nextDouble() < .1 
                    ? randomTag(tags) : randomWord(false)) + " ";
        }
        sentence += randomWord(false) + randomPunctuation(true) + " ";
        return sentence;
    }

    public String CreateTitle(String postContent)
    {
        String title = "";

        String[] words = postContent.split(" ");
        int titleLength = words[0].length();

        for (int i = 0; i < titleLength; i++)
        {
            title += words[i];
            if (i < titleLength - 1)
            {
                title += " ";
            }
        }
        return toTitleCase(title);
    }
    
    public String GetName(String title)
    {
        if (title.length() % 4 == 0)
        {
            return "Atreya";
        }
        if (title.length() % 4 == 1)
        {
            return "Doug";
        }
        if (title.length() % 4 == 2)
        {
            return "Eric";
        }
        return "Sundar";
    }

    private String randomWord(Boolean infrequent)
    {
        double randNum = rand.nextDouble();
        if (infrequent)
        {
            randNum = (randNum / 2.0) + 0.5;
        }
        for (double wordFreq : dictionaryWords.keySet())
        {
            if (randNum < wordFreq)
            {
                return dictionaryWords.get(wordFreq);
            }
        }
        return "";
    }

    private String randomTag(List<String> tags)
    {
        return tags.get(rand.nextInt(tags.size() - 1));
    }

    private String randomPunctuation(Boolean endSentence)
    {
        double randNum = rand.nextDouble();
        if (endSentence)
        {
            if (randNum < .8)
            {
                return ".";
            }
            if (randNum < .9)
            {
                return "?";
            }
            if (randNum < .98)
            {
                return "!";
            }
            return "...";
        }
        else
        {
            if (randNum < .5)
            {
                return "";
            }
            if (randNum < .9)
            {
                return ",";
            }
            return ";";
        }
    }

    private String toTitleCase(String input)
    {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray())
        {
            if (Character.isSpaceChar(c)) 
            {
                nextTitleCase = true;
            } 
            else if (nextTitleCase) 
            {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            if (Character.isAlphabetic(c) | Character.isSpaceChar(c))
            {
                titleCase.append(c);
            }
        }
    return titleCase.toString();
    }
}
