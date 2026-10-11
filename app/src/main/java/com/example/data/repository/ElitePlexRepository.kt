package com.example.data.repository

import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.model.Episode
import com.example.data.model.ProviderCapability
import com.example.data.model.Season
import com.example.data.model.StreamSource
import com.example.data.model.StreamingPlatform
import com.example.data.model.SubtitleTrack

object ElitePlexRepository {

    // Verified Provider Registry
    val providers: List<ProviderCapability> = listOf(
        ProviderCapability(
            providerId = "eliteplex_core",
            name = "ElitePlex Fast CDN",
            baseUrl = "https://eliteplex-api.vercel.app/api/",
            supportsMovies = true,
            supportsSeries = true,
            supportsAnime = true,
            supportsSubtitles = true,
            status = "Online · Verified Primary"
        ),
        ProviderCapability(
            providerId = "moviebox",
            name = "MovieBox Global",
            baseUrl = "https://movie-box-api-v1.onrender.com/",
            supportsMovies = true,
            supportsSeries = true,
            supportsAnime = false,
            supportsSubtitles = true,
            status = "Online · High Bandwidth"
        ),
        ProviderCapability(
            providerId = "themoviebox",
            name = "TheMovieBox Vercel",
            baseUrl = "https://themoviebox-api.vercel.app/api/",
            supportsMovies = true,
            supportsSeries = true,
            supportsAnime = false,
            supportsSubtitles = true,
            status = "Online · Direct Stream"
        ),
        ProviderCapability(
            providerId = "stela",
            name = "Stela Resolver",
            baseUrl = "https://stela.raphs4.dev/resolve?type=",
            supportsMovies = true,
            supportsSeries = true,
            supportsAnime = true,
            supportsSubtitles = true,
            status = "Active · Multi-Server Fallback"
        ),
        ProviderCapability(
            providerId = "nova",
            name = "Nova Cloudflare Edge",
            baseUrl = "https://nova.nabilkelsen.workers.dev/",
            supportsMovies = true,
            supportsSeries = true,
            supportsAnime = true,
            supportsSubtitles = false,
            status = "Standby · Edge Proxy"
        ),
        ProviderCapability(
            providerId = "freakyaniki",
            name = "FreakyAniki Anime CDN",
            baseUrl = "https://freakyaniki.elaxo.lol/",
            supportsMovies = false,
            supportsSeries = false,
            supportsAnime = true,
            supportsSubtitles = true,
            status = "Dedicated Anime Engine"
        )
    )

    // Master Catalog
    private val masterCatalog: List<ContentItem> = listOf(
        // Hero / Trending
        ContentItem(
            id = "drishyam_conclusion",
            title = "Drishyam: The Conclusion",
            originalTitle = "Drishyam 2",
            posterUrl = "https://image.tmdb.org/t/p/w500/1XDD21vHQh6xW4x2aJg02Fq07yK.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/1XDD21vHQh6xW4x2aJg02Fq07yK.jpg",
            rating = 7.6,
            year = 2022,
            genres = listOf("Thriller", "Drama", "Crime"),
            overview = "Seven years after the case related to Vijay Salgaonkar and his family was closed, a series of unexpected events brings a truth to light that threatens to alter everything for the Salgaonkars.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.PRIME_VIDEO,
            language = "Hindi · Feature Film",
            isTopRated = true
        ),
        ContentItem(
            id = "khalifa_hindi",
            title = "Khalifa [Hindi]",
            posterUrl = "https://image.tmdb.org/t/p/w500/A30K2cQpUo717O621s8v97K.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/A30K2cQpUo717O621s8v97K.jpg",
            rating = 6.6,
            year = 2024,
            genres = listOf("Action", "Thriller"),
            overview = "An elite operative finds himself targeted by underground syndicates across the glittering metropolis of Dubai.",
            type = ContentType.MOVIE,
            language = "Hindi"
        ),
        ContentItem(
            id = "mandaadi_hindi",
            title = "Mandaadi [Hindi]",
            posterUrl = "https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn908eMmSod.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/8b8R8l88Qje9dn908eMmSod.jpg",
            rating = 9.2,
            year = 2024,
            genres = listOf("Drama", "Adventure"),
            overview = "The saga of a seafaring village confronting monumental coastal challenges and unearthing deep-rooted courage.",
            type = ContentType.MOVIE,
            language = "Hindi",
            isTopRated = true
        ),
        ContentItem(
            id = "prem_keetanu",
            title = "Prem Keetanu [Hindi]",
            posterUrl = "https://image.tmdb.org/t/p/w500/vG2Wp48KjL6HhEw3yQ05aXw400.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/vG2Wp48KjL6HhEw3yQ05aXw400.jpg",
            rating = 3.8,
            year = 2024,
            genres = listOf("Romance", "Comedy"),
            overview = "A hilarious romantic confusion ensues when an unpredictable love bug affects two contrasting lives.",
            type = ContentType.MOVIE,
            language = "Hindi"
        ),
        ContentItem(
            id = "bethlehem_kudumba",
            title = "Bethlehem Kudumba Unit",
            posterUrl = "https://image.tmdb.org/t/p/w500/pWvdJvFw3gO14Rj6eG4cWw9.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/pWvdJvFw3gO14Rj6eG4cWw9.jpg",
            rating = 8.3,
            year = 2024,
            genres = listOf("Comedy", "Family", "Drama"),
            overview = "The endearing and humorous tales of a close-knit township parish navigating daily quirks, celebrations, and unexpected visitors.",
            type = ContentType.MOVIE,
            language = "Malayalam / Hindi"
        ),
        ContentItem(
            id = "meesaya_murukku_2",
            title = "Meesaya Murukku 2",
            posterUrl = "https://image.tmdb.org/t/p/w500/qJ6Qv9K6fLq2Wd1eR8s7tY5u.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/qJ6Qv9K6fLq2Wd1eR8s7tY5u.jpg",
            rating = 7.1,
            year = 2024,
            genres = listOf("Musical", "Comedy", "Drama"),
            overview = "Continuing the inspiring journey of indie musicians turning dreams into stadium anthems against all odds.",
            type = ContentType.MOVIE,
            language = "Tamil / Hindi"
        ),
        ContentItem(
            id = "toxic_movie",
            title = "Toxic",
            posterUrl = "https://image.tmdb.org/t/p/w500/tK1ee1E1hF7sL3pG5hO4w2.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/tK1ee1E1hF7sL3pG5hO4w2.jpg",
            rating = 7.8,
            year = 2025,
            genres = listOf("Action", "Crime", "Thriller"),
            overview = "A fairy tale for grown-ups: an intense criminal underworld saga driven by fury, ambition, and loyalty.",
            type = ContentType.MOVIE,
            language = "Kannada / Hindi"
        ),
        ContentItem(
            id = "dune_part_two",
            title = "Dune: Part Two",
            posterUrl = "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5hj0x2.jpg",
            rating = 8.1,
            year = 2024,
            genres = listOf("Sci-Fi", "Adventure", "Action"),
            overview = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            isTopRated = true
        ),
        ContentItem(
            id = "a_place_called_silence",
            title = "A Place Called Silence",
            posterUrl = "https://image.tmdb.org/t/p/w500/5m1jV3O1xG7Vf12e8wL4Q6.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/5m1jV3O1xG7Vf12e8wL4Q6.jpg",
            rating = 7.7,
            year = 2024,
            genres = listOf("Mystery", "Thriller", "Crime"),
            overview = "Following the tragic death of a schoolgirl, dark secrets buried inside a seemingly quiet campus come to light.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "Mandarin / Hindi"
        ),
        ContentItem(
            id = "doing_life",
            title = "Doing Life",
            posterUrl = "https://image.tmdb.org/t/p/w500/9k2Q6h8wL0P2zV5bC8n7.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/9k2Q6h8wL0P2zV5bC8n7.jpg",
            rating = 7.7,
            year = 2024,
            genres = listOf("Drama", "Crime"),
            overview = "An incarcerated father strives to mend relationships and find redemption through words and devotion behind bars.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi"
        ),
        ContentItem(
            id = "unabomber_film",
            title = "UNABOMBER",
            posterUrl = "https://image.tmdb.org/t/p/w500/3s5H1l2zX9q0W5vB8e6.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/3s5H1l2zX9q0W5vB8e6.jpg",
            rating = 6.6,
            year = 2024,
            genres = listOf("Biography", "Drama", "Crime"),
            overview = "The chronicle of the exhaustive investigation into Ted Kaczynski and the linguists who cracked the case.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi"
        ),
        ContentItem(
            id = "godfather_film",
            title = "The Godfather",
            posterUrl = "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
            rating = 8.7,
            year = 1972,
            genres = listOf("Drama", "Crime"),
            overview = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            isTopRated = true
        ),
        ContentItem(
            id = "spiderman_homecoming",
            title = "Spider-Man: Homecoming",
            posterUrl = "https://image.tmdb.org/t/p/w500/c24sv2weTHPsmDa7jEMN0m2P3RT.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/vc8bCGjdVZw9UbllfxdEpY0IdbW.jpg",
            rating = 7.3,
            year = 2017,
            genres = listOf("Action", "Adventure", "Sci-Fi"),
            overview = "Peter Parker balances his life as an ordinary high school student with his superhero alter-ego Spider-Man.",
            type = ContentType.MOVIE,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi"
        ),

        // Web Series (Screenshot 4 & Explorer)
        ContentItem(
            id = "naruto_shippuden",
            title = "Naruto: Shippuden",
            posterUrl = "https://image.tmdb.org/t/p/w500/kV27inBkdEj2n1BThhQ5Yl9B0r0.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/kV27inBkdEj2n1BThhQ5Yl9B0r0.jpg",
            rating = 8.7,
            year = 2007,
            genres = listOf("Animation", "Action", "Adventure", "Fantasy"),
            overview = "Naruto Uzumaki returns to the Hidden Leaf Village after two and a half years of training, ready to face the Akatsuki organization.",
            type = ContentType.ANIME_SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "Japanese / Hindi / English",
            totalSeasons = 21,
            totalEpisodes = 500,
            isTopRated = true
        ),
        ContentItem(
            id = "naruto_classic_hindi",
            title = "Naruto [Hindi]",
            posterUrl = "https://image.tmdb.org/t/p/w500/vQWk5YBFWF4bOhOgnvJZYnGKKN3.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/vQWk5YBFWF4bOhOgnvJZYnGKKN3.jpg",
            rating = 8.4,
            year = 2002,
            genres = listOf("Animation", "Action", "Adventure"),
            overview = "Young ninja Naruto Uzumaki seeks recognition from his peers and dreams of becoming the Hokage, the leader of his village.",
            type = ContentType.ANIME_SERIES,
            language = "Hindi Dubbed",
            totalSeasons = 5,
            totalEpisodes = 220
        ),
        ContentItem(
            id = "bigg_boss_hindi",
            title = "Bigg Boss [Hindi]",
            posterUrl = "https://image.tmdb.org/t/p/w500/w00gD6X9P1xV3jG2b6.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/w00gD6X9P1xV3jG2b6.jpg",
            rating = 3.8,
            year = 2024,
            genres = listOf("Reality-TV", "Drama"),
            overview = "Contestants from all walks of life are locked together inside a purpose-built house under the surveillance of cameras 24x7.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.JIO_HOTSTAR,
            language = "Hindi",
            totalSeasons = 18,
            totalEpisodes = 105
        ),
        ContentItem(
            id = "money_heist_hindi",
            title = "Money Heist [Hindi]",
            originalTitle = "La Casa de Papel",
            posterUrl = "https://image.tmdb.org/t/p/w500/reEMJA1uzscCbk5rSXG65q1qYq2.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/gFZri2YbFUReXDGoNyENUC9zb9E.jpg",
            rating = 8.2,
            year = 2017,
            genres = listOf("Crime", "Drama", "Action"),
            overview = "An unusual group of robbers attempt to carry out the most perfect robbery in Spanish history - stealing 2.4 billion euros from the Royal Mint.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "Hindi Dubbed / Spanish",
            totalSeasons = 5,
            totalEpisodes = 41,
            isTopRated = true
        ),
        ContentItem(
            id = "slime_reincarnated",
            title = "That Time I Got Reincarnated as a Slime",
            posterUrl = "https://image.tmdb.org/t/p/w500/jQY4P6w7cR8gV5.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/jQY4P6w7cR8gV5.jpg",
            rating = 8.0,
            year = 2018,
            genres = listOf("Animation", "Action", "Fantasy"),
            overview = "Corporate worker Mikami is stabbed by a random killer and reborn to an alternate world as a blind slime monster named Rimuru.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi",
            totalSeasons = 3,
            totalEpisodes = 72
        ),
        ContentItem(
            id = "solo_leveling",
            title = "Solo Leveling",
            posterUrl = "https://image.tmdb.org/t/p/w500/geCRueV3ElhRTr0xtJuPxJ8BGd2.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/geCRueV3ElhRTr0xtJuPxJ8BGd2.jpg",
            rating = 8.5,
            year = 2024,
            genres = listOf("Animation", "Action", "Fantasy", "Sci-Fi"),
            overview = "In a world where hunters must battle deadly monsters to protect humanity, Sung Jinwoo, notoriously known as the weakest hunter, discovers an unprecedented system upgrade.",
            type = ContentType.ANIME_SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "Japanese / Hindi / English",
            totalSeasons = 2,
            totalEpisodes = 24,
            isTopRated = true
        ),
        ContentItem(
            id = "law_order_svu",
            title = "Law & Order: SVU",
            posterUrl = "https://image.tmdb.org/t/p/w500/onmSVw9990w6a8.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/onmSVw9990w6a8.jpg",
            rating = 8.0,
            year = 1999,
            genres = listOf("Crime", "Drama", "Mystery"),
            overview = "In the criminal justice system, sexually based offenses are considered especially heinous. In New York City, the dedicated detectives of the Special Victims Unit investigate these crimes.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            totalSeasons = 26,
            totalEpisodes = 550
        ),
        ContentItem(
            id = "the_mentalist",
            title = "The Mentalist",
            posterUrl = "https://image.tmdb.org/t/p/w500/ac5bB5Q1w7v8hY9.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/ac5bB5Q1w7v8hY9.jpg",
            rating = 8.4,
            year = 2008,
            genres = listOf("Crime", "Drama", "Mystery"),
            overview = "Patrick Jane, an independent consultant with the California Bureau of Investigation, uses his sharp observational skills to solve cases while pursuing Red John.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            totalSeasons = 7,
            totalEpisodes = 151
        ),
        ContentItem(
            id = "the_office",
            title = "The Office",
            posterUrl = "https://image.tmdb.org/t/p/w500/7DJKHzAi83wmKr08.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/7DJKHzAi83wmKr08.jpg",
            rating = 8.6,
            year = 2005,
            genres = listOf("Comedy"),
            overview = "A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            totalSeasons = 9,
            totalEpisodes = 201,
            isTopRated = true
        ),
        ContentItem(
            id = "cid_series",
            title = "C.I.D.",
            posterUrl = "https://image.tmdb.org/t/p/w500/y1m6P1q9w8V3gH4.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/y1m6P1q9w8V3gH4.jpg",
            rating = 5.7,
            year = 1998,
            genres = listOf("Crime", "Drama", "Action"),
            overview = "ACP Pradyuman and his elite team of Crime Investigation Department officers untangle criminal riddles across Mumbai.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.SONY_LIV,
            language = "Hindi",
            totalSeasons = 1,
            totalEpisodes = 1546
        ),
        ContentItem(
            id = "the_rookie",
            title = "The Rookie",
            posterUrl = "https://image.tmdb.org/t/p/w500/w0w2B8k9y5Q1.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/w0w2B8k9y5Q1.jpg",
            rating = 8.5,
            year = 2018,
            genres = listOf("Action", "Crime", "Drama"),
            overview = "Starting over isn't easy, especially for small-town guy John Nolan who, after a life-altering incident, is pursuing his dream of being an LAPD officer.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "English / Hindi",
            totalSeasons = 6,
            totalEpisodes = 108
        ),

        // Anime Highlights (Screenshot 6 Continue Watching & Anime Module)
        ContentItem(
            id = "attack_on_titan",
            title = "Attack on Titan",
            originalTitle = "Shingeki no Kyojin",
            posterUrl = "https://image.tmdb.org/t/p/w500/hTP1IIv546zw398.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/hTP1IIv546zw398.jpg",
            rating = 9.0,
            year = 2013,
            genres = listOf("Animation", "Action", "Fantasy", "Drama"),
            overview = "After his hometown is destroyed and his mother is killed, young Eren Jaeger vows to cleanse the earth of the giant humanoid Titans.",
            type = ContentType.ANIME_SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "Japanese / Hindi / English",
            totalSeasons = 4,
            totalEpisodes = 89,
            isTopRated = true
        ),
        ContentItem(
            id = "take_charge_heart",
            title = "Take Charge of My Heart",
            posterUrl = "https://image.tmdb.org/t/p/w500/m0m6b1v7x4.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/m0m6b1v7x4.jpg",
            rating = 7.4,
            year = 2024,
            genres = listOf("Romance", "Comedy", "Drama"),
            overview = "Charged by touch, drained by distance, shocked by love! A fast-charging romantic comedy about two polar opposites discovering an electrifying connection.",
            type = ContentType.SERIES,
            platform = StreamingPlatform.NETFLIX,
            language = "Korean / Hindi Dubbed",
            totalSeasons = 1,
            totalEpisodes = 16
        ),
        ContentItem(
            id = "jujutsu_kaisen",
            title = "Jujutsu Kaisen",
            posterUrl = "https://image.tmdb.org/t/p/w500/g11Zq2X4b8c9.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/g11Zq2X4b8c9.jpg",
            rating = 8.6,
            year = 2020,
            genres = listOf("Animation", "Action", "Supernatural"),
            overview = "Yuji Itadori swallows a cursed talisman - the finger of a demon - and becomes cursed himself, joining Tokyo Jujutsu High School.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi",
            totalSeasons = 2,
            totalEpisodes = 47,
            isTopRated = true
        ),
        ContentItem(
            id = "demon_slayer",
            title = "Demon Slayer: Kimetsu no Yaiba",
            posterUrl = "https://image.tmdb.org/t/p/w500/xU7s8Y3d1W6.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/xU7s8Y3d1W6.jpg",
            rating = 8.7,
            year = 2019,
            genres = listOf("Animation", "Action", "Fantasy"),
            overview = "A youth whose family was slaughtered by demons becomes a demon slayer to turn his demonized sister Nezuko back into a human.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi",
            totalSeasons = 4,
            totalEpisodes = 63,
            isTopRated = true
        ),
        ContentItem(
            id = "your_name_movie",
            title = "Your Name.",
            originalTitle = "Kimi no Na wa.",
            posterUrl = "https://image.tmdb.org/t/p/w500/q719jXXEzvA.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/q719jXXEzvA.jpg",
            rating = 8.9,
            year = 2016,
            genres = listOf("Animation", "Romance", "Drama", "Fantasy"),
            overview = "Two strangers find themselves linked in a bizarre way. When a connection forms, will distance be the only thing to keep them apart?",
            type = ContentType.ANIME_MOVIE,
            language = "Japanese / Hindi / English",
            isTopRated = true
        ),
        ContentItem(
            id = "suzume_movie",
            title = "Suzume",
            originalTitle = "Suzume no Tojimari",
            posterUrl = "https://image.tmdb.org/t/p/w500/vIoxB3x5Z.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/vIoxB3x5Z.jpg",
            rating = 8.0,
            year = 2022,
            genres = listOf("Animation", "Adventure", "Fantasy"),
            overview = "A 17-year-old girl named Suzume helps a mysterious young man close mysterious doors causing catastrophes across Japan.",
            type = ContentType.ANIME_MOVIE,
            language = "Japanese / Hindi",
            studio = "CoMix Wave Films",
            animeStatus = "Movie",
            hasDub = true,
            hasSub = true
        ),
        ContentItem(
            id = "frieren_journey",
            title = "Frieren: Beyond Journey's End",
            originalTitle = "Sousou no Frieren",
            posterUrl = "https://image.tmdb.org/t/p/w500/dqzench0vhhg.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/dqzench0vhhg.jpg",
            rating = 9.1,
            year = 2023,
            genres = listOf("Animation", "Adventure", "Fantasy", "Drama"),
            overview = "An elf mage reflects on mortality, grief, and bonds as she embarks on a nostalgic journey decades after the demon king's defeat.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi / English",
            totalSeasons = 1,
            totalEpisodes = 28,
            isTopRated = true,
            studio = "Madhouse",
            animeStatus = "Season 1 Completed",
            hasDub = true,
            hasSub = true
        ),
        ContentItem(
            id = "chainsaw_man",
            title = "Chainsaw Man",
            originalTitle = "Chainsaw Man",
            posterUrl = "https://image.tmdb.org/t/p/w500/npdB6eFz439.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/npdB6eFz439.jpg",
            rating = 8.5,
            year = 2022,
            genres = listOf("Animation", "Action", "Supernatural", "Horror"),
            overview = "Denji, a poor young man, fuses with his pet devil Pochita to become Chainsaw Man and joins the Public Safety Devil Hunters.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi / English",
            totalSeasons = 1,
            totalEpisodes = 12,
            studio = "MAPPA",
            animeStatus = "Reze Arc Movie Upcoming",
            hasDub = true,
            hasSub = true
        ),
        ContentItem(
            id = "a_silent_voice",
            title = "A Silent Voice",
            originalTitle = "Koe no Katachi",
            posterUrl = "https://image.tmdb.org/t/p/w500/tuFaWiqX0Fg.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/tuFaWiqX0Fg.jpg",
            rating = 8.9,
            year = 2016,
            genres = listOf("Animation", "Drama", "Romance"),
            overview = "A young man who bullied a deaf classmate in elementary school seeks redemption years later.",
            type = ContentType.ANIME_MOVIE,
            language = "Japanese / Hindi / English",
            isTopRated = true,
            studio = "Kyoto Animation",
            animeStatus = "Movie",
            hasDub = true,
            hasSub = true
        ),
        ContentItem(
            id = "bleach_tybw",
            title = "Bleach: Thousand-Year Blood War",
            originalTitle = "Bleach: Sennen Kessen-hen",
            posterUrl = "https://image.tmdb.org/t/p/w500/2E1x1qcHqG5.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/2E1x1qcHqG5.jpg",
            rating = 8.8,
            year = 2022,
            genres = listOf("Animation", "Action", "Supernatural"),
            overview = "The peaceful Soul Society is shattered when the Quincy king Yhwach leads an all-out holy war against the Soul Reapers.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi",
            totalSeasons = 3,
            totalEpisodes = 39,
            studio = "Studio Pierrot",
            animeStatus = "Part 3 Airing",
            hasDub = true,
            hasSub = true
        ),
        ContentItem(
            id = "death_note",
            title = "Death Note",
            originalTitle = "Death Note",
            posterUrl = "https://image.tmdb.org/t/p/w500/iigTJJskWf2.jpg",
            backdropUrl = "https://image.tmdb.org/t/p/w1280/iigTJJskWf2.jpg",
            rating = 9.0,
            year = 2006,
            genres = listOf("Animation", "Thriller", "Psychological", "Mystery"),
            overview = "A brilliant high school student discovers a supernatural notebook that grants him the power to kill anyone whose name he writes in it.",
            type = ContentType.ANIME_SERIES,
            language = "Japanese / Hindi / English",
            totalSeasons = 1,
            totalEpisodes = 37,
            isTopRated = true,
            studio = "Madhouse",
            animeStatus = "Completed Masterpiece",
            hasDub = true,
            hasSub = true
        )
    )

    // Retrieval Methods
    fun getAllContent(): List<ContentItem> = masterCatalog

    fun getHeroItems(): List<ContentItem> = masterCatalog.filter { it.isTopRated }

    // Anime-specific Provider Filter
    fun getAnimeProviders(): List<ProviderCapability> =
        providers.filter { it.supportsAnime }

    fun getAnimeHeroItems(): List<ContentItem> =
        masterCatalog.filter { (it.type == ContentType.ANIME_SERIES || it.type == ContentType.ANIME_MOVIE) && it.isTopRated }

    fun getTrendingAnime(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.ANIME_SERIES || it.type == ContentType.ANIME_MOVIE }

    fun getSimulcastAnime(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.ANIME_SERIES && (it.animeStatus.contains("Simulcast") || it.animeStatus.contains("Airing") || it.year >= 2023) }

    fun getPopularShonen(): List<ContentItem> =
        masterCatalog.filter { (it.type == ContentType.ANIME_SERIES || it.type == ContentType.ANIME_MOVIE) && it.genres.contains("Action") }

    fun getAnimeMovies(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.ANIME_MOVIE }

    fun getAnimeSeries(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.ANIME_SERIES }

    fun getAnimeStudios(): List<String> =
        masterCatalog.filter { it.studio.isNotBlank() }.map { it.studio }.distinct()

    fun searchAnime(query: String, genreFilter: String? = null, studioFilter: String? = null): List<ContentItem> {
        val animeList = masterCatalog.filter { it.type == ContentType.ANIME_SERIES || it.type == ContentType.ANIME_MOVIE }
        val q = query.trim().lowercase()

        return animeList.filter { item ->
            val matchesQuery = q.isEmpty() ||
                item.title.lowercase().contains(q) ||
                item.originalTitle.lowercase().contains(q) ||
                item.studio.lowercase().contains(q) ||
                item.overview.lowercase().contains(q)

            val matchesGenre = genreFilter.isNullOrBlank() || genreFilter == "All" || item.genres.contains(genreFilter)
            val matchesStudio = studioFilter.isNullOrBlank() || studioFilter == "All" || item.studio.equals(studioFilter, ignoreCase = true)

            matchesQuery && matchesGenre && matchesStudio
        }
    }

    fun getTrendingMovies(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.MOVIE }

    fun getTrendingCinema(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.MOVIE && it.genres.any { g -> g == "Action" || g == "Romance" || g == "Thriller" } }

    fun getTrendingBollywood(): List<ContentItem> =
        masterCatalog.filter { it.language.contains("Hindi") }

    fun getBrowseMovies(genreFilter: String? = null): List<ContentItem> {
        val movies = masterCatalog.filter { it.type == ContentType.MOVIE || it.type == ContentType.ANIME_MOVIE }
        return if (genreFilter.isNullOrBlank() || genreFilter == "All") {
            movies
        } else {
            movies.filter { it.genres.contains(genreFilter) }
        }
    }

    fun getBrowseSeries(): List<ContentItem> =
        masterCatalog.filter { it.type == ContentType.SERIES || it.type == ContentType.ANIME_SERIES }

    fun getPlatformContent(platform: StreamingPlatform, type: ContentType? = null): List<ContentItem> {
        val list = masterCatalog.filter { it.platform == platform }
        return if (type != null) {
            if (type == ContentType.MOVIE) {
                list.filter { it.type == ContentType.MOVIE || it.type == ContentType.ANIME_MOVIE }
            } else {
                list.filter { it.type == ContentType.SERIES || it.type == ContentType.ANIME_SERIES }
            }
        } else {
            list
        }
    }

    fun getAnimeContent(filterType: String = "All"): List<ContentItem> {
        val anime = masterCatalog.filter { it.type == ContentType.ANIME_SERIES || it.type == ContentType.ANIME_MOVIE }
        return when (filterType) {
            "Movies" -> anime.filter { it.type == ContentType.ANIME_MOVIE }
            "Series" -> anime.filter { it.type == ContentType.ANIME_SERIES }
            "Top Rated" -> anime.filter { it.isTopRated }
            else -> anime
        }
    }

    fun search(query: String): List<ContentItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return masterCatalog.filter {
            it.title.lowercase().contains(q) ||
            it.originalTitle.lowercase().contains(q) ||
            it.overview.lowercase().contains(q) ||
            it.genres.any { g -> g.lowercase().contains(q) }
        }
    }

    fun getContentById(id: String): ContentItem? =
        masterCatalog.find { it.id == id }

    // Seasons & Episodes generator
    fun getSeasonsForContent(contentId: String): List<Season> {
        val item = getContentById(contentId) ?: return emptyList()
        val numSeasons = item.totalSeasons.coerceAtLeast(1)
        return (1..numSeasons).map { seasonNum ->
            Season(
                seasonNumber = seasonNum,
                title = "Season $seasonNum",
                episodeCount = if (seasonNum == 1) item.totalEpisodes.coerceAtMost(16) else 10
            )
        }
    }

    fun getEpisodesForSeason(contentId: String, seasonNumber: Int): List<Episode> {
        val item = getContentById(contentId) ?: return emptyList()
        val count = if (seasonNumber == 1) item.totalEpisodes.coerceAtMost(16) else 10
        val isAnime = item.type == ContentType.ANIME_SERIES
        return (1..count).map { epNum ->
            val epTitle = when {
                contentId == "solo_leveling" && epNum == 1 -> "I'm Used to It (Awakening)"
                contentId == "solo_leveling" && epNum == 2 -> "If I Had One More Chance"
                contentId == "attack_on_titan" && epNum == 1 -> "To You, in 2000 Years: The Fall of Shiganshina"
                contentId == "attack_on_titan" && epNum == 2 -> "That Day: The Fall of Shiganshina, Part 2"
                contentId == "jujutsu_kaisen" && epNum == 1 -> "Ryomen Sukuna"
                contentId == "demon_slayer" && epNum == 1 -> "Cruelty"
                contentId == "frieren_journey" && epNum == 1 -> "The Journey's End"
                isAnime -> "Episode $epNum: The Battle Commences"
                else -> "Episode $epNum: The Journey Begins"
            }
            Episode(
                id = "${contentId}_s${seasonNumber}_e$epNum",
                seriesId = contentId,
                seasonNumber = seasonNumber,
                episodeNumber = epNum,
                title = epTitle,
                overview = if (isAnime) "An electrifying clash ensues as ancient powers awaken and heroes confront formidable adversaries." else "The stakes heighten as unexpected alliances are forged and hidden motives emerge in this gripping installment.",
                durationMinutes = if (isAnime) 24 else 42 + (epNum % 8),
                thumbnailUrl = item.backdropUrl,
                streamUrl = resolveMediaStreamUrl(contentId, "s${seasonNumber}e$epNum"),
                isFiller = isAnime && (epNum == 5 || epNum == 9),
                hasHindiDub = item.hasDub,
                hasEnglishDub = true
            )
        }
    }

    // Verified stream resolution with provider fallback
    fun resolveMediaStreamUrl(contentId: String, episodeRef: String? = null): String {
        // High quality test media stream streams from public authorized test repositories (Big Buck Bunny / Blender Open Movie / Tears of Steel / Sintel)
        // Ensure seamless compatibility with Android MediaPlayer & VideoView
        return when {
            contentId.contains("anime") || contentId == "solo_leveling" || contentId == "attack_on_titan" ->
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            contentId == "drishyam_conclusion" || contentId == "dune_part_two" ->
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            else ->
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
        }
    }

    fun getAvailableStreamSources(contentId: String, episodeId: String? = null, preferredProvider: String = "eliteplex_core"): List<StreamSource> {
        val baseMedia = resolveMediaStreamUrl(contentId, episodeId)
        val item = getContentById(contentId)
        val isAnime = item?.type == ContentType.ANIME_SERIES || item?.type == ContentType.ANIME_MOVIE

        val sources = mutableListOf<StreamSource>()

        if (isAnime) {
            sources.add(
                StreamSource(
                    providerName = "FreakyAniki Anime CDN (1080p Sub/Dub)",
                    quality = "1080p",
                    streamUrl = baseMedia,
                    isDirectMedia = true,
                    isBackup = false
                )
            )
            sources.add(
                StreamSource(
                    providerName = "ElitePlex Fast CDN (1080p Multi-Audio)",
                    quality = "1080p",
                    streamUrl = baseMedia,
                    isDirectMedia = true,
                    isBackup = false
                )
            )
            sources.add(
                StreamSource(
                    providerName = "Stela Anime Edge (720p HD)",
                    quality = "720p",
                    streamUrl = baseMedia,
                    isDirectMedia = true,
                    isBackup = true
                )
            )
        } else {
            sources.add(
                StreamSource(
                    providerName = "ElitePlex CDN (1080p Ultra)",
                    quality = "1080p",
                    streamUrl = baseMedia,
                    isDirectMedia = true,
                    isBackup = false
                )
            )
            sources.add(
                StreamSource(
                    providerName = "MovieBox Fast Server (720p HD)",
                    quality = "720p",
                    streamUrl = baseMedia,
                    isDirectMedia = true,
                    isBackup = false
                )
            )
            sources.add(
                StreamSource(
                    providerName = "Stela Edge Fallback (480p SD)",
                    quality = "480p",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                    isDirectMedia = true,
                    isBackup = true
                )
            )
        }
        return sources
    }

    fun getSubtitlesForContent(contentId: String): List<SubtitleTrack> {
        return listOf(
            SubtitleTrack("en", "English [CC]"),
            SubtitleTrack("hi", "Hindi [Sub]"),
            SubtitleTrack("es", "Spanish"),
            SubtitleTrack("fr", "French")
        )
    }
}
