package com.example.data

object BuiltInChannelsCatalog {

    val freeApiProviders: List<FreeApiProvider> = listOf(
        FreeApiProvider(
            id = "ffxapis_icx",
            name = "FFxAPIs / IcX Core Stream Index",
            description = "Verified HTTP 200/206 HLS live streams for Bangladesh & India with multi-server failover.",
            endpointUrl = "https://iptv-org.github.io/iptv/countries/bd.m3u",
            regionTag = "BD & IN Core",
            format = "JSON / HLS",
            estimatedChannels = "41+ Verified",
            isPreloaded = true
        ),
        FreeApiProvider(
            id = "iptv_org_bd",
            name = "IPTV-Org Bangladesh Live API",
            description = "Open-source community M3U feed for Bangladeshi news, sports, and entertainment networks.",
            endpointUrl = "https://iptv-org.github.io/iptv/countries/bd.m3u",
            regionTag = "Bangladesh 🇧🇩",
            format = "M3U8 Playlist",
            estimatedChannels = "55+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_in",
            name = "IPTV-Org India Master API",
            description = "Live Indian regional, Hindi, Bengali, Tamil, music, and national sports HLS feeds.",
            endpointUrl = "https://iptv-org.github.io/iptv/countries/in.m3u",
            regionTag = "India 🇮🇳",
            format = "M3U8 Playlist",
            estimatedChannels = "250+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_animation",
            name = "Global Anime & Animation Stream API",
            description = "Curated international animation, anime, cartoons, and retro gaming live broadcast channels.",
            endpointUrl = "https://iptv-org.github.io/iptv/categories/animation.m3u",
            regionTag = "Anime & Kids ⚡",
            format = "M3U8 Playlist",
            estimatedChannels = "45+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_sports",
            name = "Global Sports Arena M3U API",
            description = "Free-to-air football, cricket, motorsport, combat sports, and athletic television feeds.",
            endpointUrl = "https://iptv-org.github.io/iptv/categories/sports.m3u",
            regionTag = "Sports 🏆",
            format = "M3U8 Playlist",
            estimatedChannels = "120+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_music",
            name = "World Music & Concerts API",
            description = "24/7 nonstop music videos, hip-hop, rock, K-pop, Bollywood hits, and electronic beats.",
            endpointUrl = "https://iptv-org.github.io/iptv/categories/music.m3u",
            regionTag = "Music 🎵",
            format = "M3U8 Playlist",
            estimatedChannels = "140+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_movies",
            name = "Cinema & Action Movies API",
            description = "Free live movie channels broadcasting action, thriller, classic cinema, and world films.",
            endpointUrl = "https://iptv-org.github.io/iptv/categories/movies.m3u",
            regionTag = "Movies 🎬",
            format = "M3U8 Playlist",
            estimatedChannels = "95+ Live"
        ),
        FreeApiProvider(
            id = "iptv_org_news",
            name = "World Breaking News 24/7 API",
            description = "International English and regional 24-hour live news networks from around the globe.",
            endpointUrl = "https://iptv-org.github.io/iptv/categories/news.m3u",
            regionTag = "Global News 🌍",
            format = "M3U8 Playlist",
            estimatedChannels = "300+ Live"
        ),
        FreeApiProvider(
            id = "free_tv_global",
            name = "Free-TV Global Verified M3U",
            description = "Curated high-uptime public broadcast television streams maintained by Free-TV.",
            endpointUrl = "https://raw.githubusercontent.com/Free-TV/IPTV/master/playlist.m3u8",
            regionTag = "Worldwide 🌐",
            format = "M3U8 Master",
            estimatedChannels = "500+ Live"
        )
    )

    val initialChannels: List<TvChannel> = listOf(
        // ================= BANGLADESH (FFxAPIs/IcX Core 1..16 + Extra BD) =================
        TvChannel(
            id = 1,
            name = "Ananda TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 1,
                    url = "https://app.ncare.live/c3VydmVyX8RpbEU9Mi8xNy8yMDE0GIDU6RgzQ6NTAgdEoaeFzbF92YWxIZTO0U0ezN1IzMyfvcGVMZEJCTEFWeVN3PTOmdFsaWRtaW51aiPhnPTI2/anandatv.stream/live-orgin/anandatv.stream/playlist.m3u8",
                    label = "NCare Live BD #1"
                )
            )
        ),
        TvChannel(
            id = 2,
            name = "Bangla Vision",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 2,
                    url = "https://tvsen5.aynaott.com/banglavision/index.m3u8",
                    label = "AynaOTT HD #1"
                )
            )
        ),
        TvChannel(
            id = 3,
            name = "Boishakhi TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 3,
                    url = "https://boishakhi.sonarbanglatv.com/boishakhi/boishakhitv/index.m3u8",
                    label = "SonarBangla HD"
                )
            )
        ),
        TvChannel(
            id = 4,
            name = "Channel S",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 4,
                    url = "https://app.ncare.live/c3VydmVyX8RpbEU9Mi8xNy8yMDE0GIDU6RgzQ6NTAgdEoaeFzbF92YWxIZTO0U0ezN1IzMyfvcGVMZEJCTEFWeVN3PTOmdFsaWRtaW51aiPhnPTI2/channels.stream/live-orgin/channels.stream/playlist.m3u8",
                    label = "NCare Origin"
                )
            )
        ),
        TvChannel(
            id = 5,
            name = "Duronto TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.KIDS,
            streams = listOf(
                StreamSource(
                    id = 5,
                    url = "https://tvsen6.aynaott.com/6xyZ3N4oHv2KBJdB6W4p/index.m3u8",
                    label = "AynaOTT Kids HD"
                )
            )
        ),
        TvChannel(
            id = 6,
            name = "Gazi TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.SPORTS,
            streams = listOf(
                StreamSource(
                    id = 6,
                    url = "https://app.ncare.live/c3VydmVyX8RpbEU9Mi8xNy8yMDE0GIDU6RgzQ6NTAgdEoaeFzbF92YWxIZTO0U0ezN1IzMyfvcGVMZEJCTEFWeVN3PTOmdFsaWRtaW51aiPhnPTI2/gazibdz.stream/live-orgin/gazibdz.stream/playlist.m3u8",
                    label = "NCare GTV Sports"
                )
            )
        ),
        TvChannel(
            id = 7,
            name = "Green TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 7,
                    url = "https://app.ncare.live/c3VydmVyX8RpbEU9Mi8xNy8yMDE0GIDU6RgzQ6NTAgdEoaeFzbF92YWxIZTO0U0ezN1IzMyfvcGVMZEJCTEFWeVN3PTOmdFsaWRtaW51aiPhnPTI2/greentv.stream/live-orgin/greentv.stream/playlist.m3u8",
                    label = "NCare Green HD"
                )
            )
        ),
        TvChannel(
            id = 8,
            name = "Maasranga TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 8,
                    url = "https://tvsen5.aynaott.com/maasrangatv/index.m3u8",
                    label = "AynaOTT Primary"
                ),
                StreamSource(
                    id = 9,
                    url = "https://tvsen5.aynascope.net/maasrangatv/index.m3u8",
                    label = "AynaScope Mirror"
                )
            )
        ),
        TvChannel(
            id = 9,
            name = "Madani Channel Bangla",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.RELIGIOUS,
            streams = listOf(
                StreamSource(
                    id = 10,
                    url = "https://streaming.madanichannel.tv/static/streaming-playlists/hls/d3e49b76-ac06-4689-a641-9200445b647f/master.m3u8",
                    label = "Madani Official HLS"
                )
            )
        ),
        TvChannel(
            id = 10,
            name = "My TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 11,
                    url = "https://app.ncare.live/c3VydmVyX8RpbEU9Mi8xNy8yMDE0GIDU6RgzQ6NTAgdEoaeFzbF92YWxIZTO0U0ezN1IzMyfvcGVMZEJCTEFWeVN3PTOmdFsaWRtaW51aiPhnPTI2/mytv-up-off.stream/live-orgin/mytv-up-off.stream/playlist.m3u8",
                    label = "NCare MyTV Live"
                )
            )
        ),
        TvChannel(
            id = 11,
            name = "Nexus TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 12,
                    url = "https://tvsen6.aynaott.com/Epm7WrFa/index.m3u8",
                    label = "AynaOTT Nexus HD"
                )
            )
        ),
        TvChannel(
            id = 12,
            name = "NTV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 13,
                    url = "https://tvsen5.aynaott.com/xV4jEKf3D9zc/index.m3u8",
                    label = "AynaOTT NTV HD"
                )
            )
        ),
        TvChannel(
            id = 13,
            name = "Peace TV Bangla",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.RELIGIOUS,
            streams = listOf(
                StreamSource(
                    id = 14,
                    url = "https://dzkyvlfyge.erbvr.com/PeaceTvBangla/index.m3u8",
                    label = "PeaceTV CDN"
                )
            )
        ),
        TvChannel(
            id = 14,
            name = "Rajdhani TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 15,
                    url = "https://stream.shariarsuvo.com/hls5/rajdhanicable.m3u8",
                    label = "Rajdhani Cable HLS"
                )
            )
        ),
        TvChannel(
            id = 15,
            name = "RTV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 16,
                    url = "https://tvsen5.aynaott.com/RtvHD/index.m3u8",
                    label = "AynaOTT RTV HD"
                ),
                StreamSource(
                    id = 17,
                    url = "https://tvsen5.aynascope.net/RtvHD/index.m3u8",
                    label = "AynaScope RTV Mirror"
                )
            )
        ),
        TvChannel(
            id = 16,
            name = "T Sports",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.SPORTS,
            streams = listOf(
                StreamSource(
                    id = 18,
                    url = "https://tvsen5.aynaott.com/TnMn5kZz8aLm/index.m3u8",
                    label = "T Sports HD Primary"
                )
            )
        ),

        // ================= INDIA (FFxAPIs/IcX Core 17..41) =================
        TvChannel(
            id = 17,
            name = "7S Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            streams = listOf(
                StreamSource(
                    id = 19,
                    url = "https://mumt03.tangotv.in/Dsly5z3H7SMUSIC/index.m3u8",
                    label = "TangoTV Mumbai #3"
                )
            )
        ),
        TvChannel(
            id = 18,
            name = "Aaj Tak",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 20,
                    url = "https://feeds.intoday.in/aajtak/api/aajtakhd/master.m3u8",
                    label = "IndiaToday Official HD"
                ),
                StreamSource(
                    id = 21,
                    url = "https://livehub-voidnet.onrender.com/cluster/streamcore/in/AAJTAK_REDIS.m3u8",
                    label = "VoidNet Cluster #2"
                ),
                StreamSource(
                    id = 22,
                    url = "https://d1rc86nwwc9fag.cloudfront.net/vglive-sk-791258/master.m3u8",
                    label = "CloudFront Edge #3"
                )
            )
        ),
        TvChannel(
            id = 19,
            name = "ABP Ananda",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 23,
                    url = "https://d2l4ar6y3mrs4k.cloudfront.net/live-streaming/ananda-livetv/master.m3u8",
                    label = "CloudFront Ananda HD"
                )
            )
        ),
        TvChannel(
            id = 20,
            name = "ABP Asmita",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 24,
                    url = "https://d2l4ar6y3mrs4k.cloudfront.net/live-streaming/asmita-livetv/master.m3u8",
                    label = "CloudFront Asmita HD"
                )
            )
        ),
        TvChannel(
            id = 21,
            name = "ABP Ganga",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 25,
                    url = "https://d2l4ar6y3mrs4k.cloudfront.net/live-streaming/ganga-livetv/master.m3u8",
                    label = "CloudFront Ganga HD"
                )
            )
        ),
        TvChannel(
            id = 22,
            name = "ABP Majha",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 26,
                    url = "https://yupprestreamliveus.akamaized.net/vglive-sk-355289/majha/master.m3u8",
                    label = "Akamai YuppLive"
                )
            )
        ),
        TvChannel(
            id = 23,
            name = "ABP News",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 27,
                    url = "https://d2l4ar6y3mrs4k.cloudfront.net/live-streaming/abpnews-livetv/master.m3u8",
                    label = "CloudFront Primary HD"
                ),
                StreamSource(
                    id = 28,
                    url = "https://d1rc86nwwc9fag.cloudfront.net/vglive-sk-472500/abpnews/master.m3u8",
                    label = "VG-Live Mirror #2"
                )
            )
        ),
        TvChannel(
            id = 24,
            name = "Anand TV",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 29,
                    url = "https://live.legitpro.co.in/anandtv/index.m3u8",
                    label = "LegitPro Live"
                )
            )
        ),
        TvChannel(
            id = 25,
            name = "Asianet Movies",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            streams = listOf(
                StreamSource(
                    id = 30,
                    url = "https://da86m1sqpm3o0.cloudfront.net/28072023/smil:asianetmovies1.smil/playlist.m3u8",
                    label = "CloudFront Cinema"
                )
            )
        ),
        TvChannel(
            id = 26,
            name = "Awaaz India TV",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 31,
                    url = "https://awaazindia.livebox.co.in/AwaazIndaTVhls/Live.m3u8",
                    label = "LiveBox HLS"
                )
            )
        ),
        TvChannel(
            id = 27,
            name = "CNBC TV18 Prime HD",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 32,
                    url = "https://n18syndication.akamaized.net/bpk-tv/CNBC_Tv18_Prime_HD_NW18_MOB/output01/index.m3u8",
                    label = "NW18 Akamai HD"
                )
            )
        ),
        TvChannel(
            id = 28,
            name = "DD Sports",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.SPORTS,
            streams = listOf(
                StreamSource(
                    id = 33,
                    url = "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/b17adfe543354fdd8d189b110617cddd/index.m3u8",
                    label = "Prasar Bharati CDN"
                )
            )
        ),
        TvChannel(
            id = 29,
            name = "Dhoom Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            streams = listOf(
                StreamSource(
                    id = 34,
                    url = "https://mumt06.tangotv.in/qYyB8fXVDHOOMMUSIC/index.m3u8",
                    label = "TangoTV Dhoom HD"
                )
            )
        ),
        TvChannel(
            id = 30,
            name = "Epic Bharat",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.LIFESTYLE,
            streams = listOf(
                StreamSource(
                    id = 35,
                    url = "https://mumt06.tangotv.in/qYyB8fXVEPICTV/index.m3u8",
                    label = "TangoTV Epic"
                )
            )
        ),
        TvChannel(
            id = 31,
            name = "Epic Bharat Digital",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 36,
                    url = "https://cc-p1izg43bk7sj5.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-p1izg43bk7sj5/DIYC/PMSL/IN10/Nazara_IN_B/Nazara_IN_B.m3u8",
                    label = "Akamai IN10 Primary"
                ),
                StreamSource(
                    id = 37,
                    url = "https://epiconvh.akamaized.net/live/nazara/master.m3u8",
                    label = "EpicON Nazara Mirror"
                )
            )
        ),
        TvChannel(
            id = 32,
            name = "Epic Bhojpuri",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 38,
                    url = "https://mumt01.tangotv.in/O5aw8Zn3EPICBHOJPURI/index.m3u8",
                    label = "TangoTV Bhojpuri"
                )
            )
        ),
        TvChannel(
            id = 33,
            name = "Epic Bhojpuri Digital",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 39,
                    url = "https://cc-8hy4a26pz2uos.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-8hy4a26pz2uos/playlist.m3u8",
                    label = "Akamai Digital HD"
                )
            )
        ),
        TvChannel(
            id = 34,
            name = "Epic Crimes",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            streams = listOf(
                StreamSource(
                    id = 40,
                    url = "https://cc-wsuyg2uxeak04.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-wsuyg2uxeak04/master.m3u8",
                    label = "Akamai Crime Investigation"
                )
            )
        ),
        TvChannel(
            id = 35,
            name = "Epic Kids Digital",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.KIDS,
            streams = listOf(
                StreamSource(
                    id = 41,
                    url = "https://cc-t8lqe1o99pszu.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-t8lqe1o99pszu/playlist.m3u8",
                    label = "Akamai Kids Animation"
                )
            )
        ),
        TvChannel(
            id = 36,
            name = "Epic Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            streams = listOf(
                StreamSource(
                    id = 42,
                    url = "https://mumt04.tangotv.in/m18aqlK4EPICMUSIC/index.m3u8",
                    label = "TangoTV Edge #1"
                ),
                StreamSource(
                    id = 43,
                    url = "https://streams.tangotv.in/EPICMUSIC/ORIGIN/index.m3u8",
                    label = "TangoTV Origin #2"
                )
            )
        ),
        TvChannel(
            id = 37,
            name = "Epic Music Digital",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            streams = listOf(
                StreamSource(
                    id = 44,
                    url = "https://cc-3cyxq80qusspd.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-3cyxq80qusspd/playlist.m3u8",
                    label = "Akamai Music Digital"
                )
            )
        ),
        TvChannel(
            id = 38,
            name = "Epic TV Digital",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.LIFESTYLE,
            streams = listOf(
                StreamSource(
                    id = 45,
                    url = "https://cc-czbq30x55knit.akamaized.net/v1/master/3722c60a815c199d9c0ef36c5b73da68a62b09d1/cc-czbq30x55knit/DIYC/PMSL/IN10/Epic_TV_IN_B/Epic_TV_IN_B.m3u8",
                    label = "Akamai Epic TV IN10"
                )
            )
        ),
        TvChannel(
            id = 39,
            name = "First India News",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            streams = listOf(
                StreamSource(
                    id = 46,
                    url = "https://xlbor37ydvaj-hls-live.wmncdn.net/firstindianewstv1/live.stream/index.m3u8",
                    label = "WMNCDN Live #1"
                ),
                StreamSource(
                    id = 47,
                    url = "https://mumt03.tangotv.in/Dsly5z3H1STINDIANEWS/index.m3u8",
                    label = "TangoTV Mirror #2"
                )
            )
        ),
        TvChannel(
            id = 40,
            name = "Food Food",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.LIFESTYLE,
            streams = listOf(
                StreamSource(
                    id = 48,
                    url = "https://mumt03.tangotv.in/Dsly5z3HFOODFOOD/index.m3u8",
                    label = "TangoTV Culinary HD"
                )
            )
        ),
        TvChannel(
            id = 41,
            name = "Goldmines",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            streams = listOf(
                StreamSource(
                    id = 49,
                    url = "https://streams.tangotv.in/GOLDMINES/ORIGIN/index.m3u8",
                    label = "Goldmines Blockbuster Origin"
                )
            )
        ),

        // ================= EXPANDED FREE API CHANNELS (42..90) =================
        TvChannel(
            id = 42,
            name = "Goldmines Bollywood",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "TangoTV / Free API",
            streams = listOf(
                StreamSource(
                    id = 50,
                    url = "https://mumt03.tangotv.in/Dsly5z3HGOLDMINESBOLLYWOOD/index.m3u8",
                    label = "TangoTV Bollywood HD"
                )
            )
        ),
        TvChannel(
            id = 43,
            name = "Goldmines Movies",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "TangoTV / Free API",
            streams = listOf(
                StreamSource(
                    id = 51,
                    url = "https://mumt03.tangotv.in/Dsly5z3HGOLDMINESMOVIES/index.m3u8",
                    label = "TangoTV Action Cinema"
                )
            )
        ),
        TvChannel(
            id = 44,
            name = "9XM Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 52,
                    url = "https://d2q8p4pe5spbak.cloudfront.net/bpk-tv/9XM/9XM.isml/index.m3u8",
                    label = "CloudFront 9XM Official"
                )
            )
        ),
        TvChannel(
            id = 45,
            name = "9X Jalwa",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 53,
                    url = "https://d2q8p4pe5spbak.cloudfront.net/bpk-tv/9XJalwa/9XJalwa.isml/index.m3u8",
                    label = "CloudFront 9X Jalwa"
                )
            )
        ),
        TvChannel(
            id = 46,
            name = "9X Tashan",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 54,
                    url = "https://d2q8p4pe5spbak.cloudfront.net/bpk-tv/9XTashan/9XTashan.isml/index.m3u8",
                    label = "CloudFront Punjabi Beats"
                )
            )
        ),
        TvChannel(
            id = 47,
            name = "B4U Movies India",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 55,
                    url = "https://cdnb4u.wiseplayout.com/B4U_Movies/master.m3u8",
                    label = "WisePlayout B4U Cinema"
                )
            )
        ),
        TvChannel(
            id = 48,
            name = "B4U Music India",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 56,
                    url = "https://cdnb4u.wiseplayout.com/B4U_Music/master.m3u8",
                    label = "WisePlayout B4U Music"
                )
            )
        ),
        TvChannel(
            id = 49,
            name = "B4U Kadak",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 57,
                    url = "https://cdnb4u.wiseplayout.com/B4U_Kadak/master.m3u8",
                    label = "WisePlayout Kadak Action"
                )
            )
        ),
        TvChannel(
            id = 50,
            name = "B4U Bhojpuri",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 58,
                    url = "https://cdnb4u.wiseplayout.com/B4U_Bhojpuri/master.m3u8",
                    label = "WisePlayout Bhojpuri"
                )
            )
        ),
        TvChannel(
            id = 51,
            name = "India Today HD",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 59,
                    url = "https://feeds.intoday.in/hltapps/api/master.m3u8",
                    label = "IndiaToday English HD"
                )
            )
        ),
        TvChannel(
            id = 52,
            name = "NDTV 24x7",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 60,
                    url = "https://ndtv24x7elemarchana.akamaized.net/hls/live/2003678/ndtv24x7/master.m3u8",
                    label = "Akamai NDTV 24x7"
                )
            )
        ),
        TvChannel(
            id = 53,
            name = "NDTV India",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 61,
                    url = "https://ndtvindiaelemarchana.akamaized.net/hls/live/2003679/ndtvindia/master.m3u8",
                    label = "Akamai NDTV Hindi"
                )
            )
        ),
        TvChannel(
            id = 54,
            name = "DD National HD",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 62,
                    url = "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/40492a64c1db4a1385ba1a397d357d3a/index.m3u8",
                    label = "CloudFront DD National"
                )
            )
        ),
        TvChannel(
            id = 55,
            name = "DD News HD",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 63,
                    url = "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/0811cd8c37ca4c409d5385a6cd2fa18b/index.m3u8",
                    label = "CloudFront DD News"
                )
            )
        ),
        TvChannel(
            id = 56,
            name = "DD India English",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 64,
                    url = "https://d3qs3d2rkhfqrt.cloudfront.net/out/v1/56b06de24f6a4427ab71522b6d902263/index.m3u8",
                    label = "CloudFront DD Global"
                )
            )
        ),
        TvChannel(
            id = 57,
            name = "WION World News",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 65,
                    url = "https://d7x8z4yuq42qn.cloudfront.net/index_7.m3u8",
                    label = "WION CloudFront HD"
                )
            )
        ),
        TvChannel(
            id = 58,
            name = "Mastiii Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IPTV-Org India API",
            streams = listOf(
                StreamSource(
                    id = 66,
                    url = "https://d35j504z0x2vu2.cloudfront.net/v1/master/0bc8e8376bd8417a1b6761138aa41c26c7309312/mastiii/master.m3u8",
                    label = "Mastiii Hits HD"
                )
            )
        ),
        TvChannel(
            id = 59,
            name = "ShowBox Music",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MUSIC,
            apiOrigin = "IN10 Media API",
            streams = listOf(
                StreamSource(
                    id = 67,
                    url = "https://epiconvh.akamaized.net/live/showbox/master.m3u8",
                    label = "EpicON ShowBox HD"
                )
            )
        ),
        TvChannel(
            id = 60,
            name = "Filamchi Bhojpuri Cinema",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.MOVIES,
            apiOrigin = "IN10 Media API",
            streams = listOf(
                StreamSource(
                    id = 68,
                    url = "https://epiconvh.akamaized.net/live/filamchi/master.m3u8",
                    label = "EpicON Filamchi HD"
                )
            )
        ),
        TvChannel(
            id = 61,
            name = "Gubbare Kids Animation",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.KIDS,
            apiOrigin = "IN10 Media API",
            streams = listOf(
                StreamSource(
                    id = 69,
                    url = "https://epiconvh.akamaized.net/live/gubbare/master.m3u8",
                    label = "EpicON Gubbare Kids HD"
                )
            )
        ),
        TvChannel(
            id = 62,
            name = "Ishara TV Drama",
            country = "India",
            countryCode = "in",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IN10 Media API",
            streams = listOf(
                StreamSource(
                    id = 70,
                    url = "https://epiconvh.akamaized.net/live/ishara/master.m3u8",
                    label = "EpicON Ishara HD"
                )
            )
        ),
        TvChannel(
            id = 63,
            name = "Jamuna TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 71,
                    url = "https://tvsen5.aynaott.com/jamunatv/index.m3u8",
                    label = "AynaOTT Jamuna 24/7"
                )
            )
        ),
        TvChannel(
            id = 64,
            name = "Somoy TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 72,
                    url = "https://tvsen5.aynaott.com/somoytv/index.m3u8",
                    label = "AynaOTT Somoy News"
                )
            )
        ),
        TvChannel(
            id = 65,
            name = "Independent TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 73,
                    url = "https://tvsen5.aynaott.com/independenttv/index.m3u8",
                    label = "AynaOTT Independent HD"
                )
            )
        ),
        TvChannel(
            id = 66,
            name = "Ekattor TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 74,
                    url = "https://tvsen5.aynaott.com/ekattortv/index.m3u8",
                    label = "AynaOTT Ekattor 71"
                )
            )
        ),
        TvChannel(
            id = 67,
            name = "Channel 24 BD",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 75,
                    url = "https://tvsen5.aynaott.com/channel24/index.m3u8",
                    label = "AynaOTT Channel 24"
                )
            )
        ),
        TvChannel(
            id = 68,
            name = "DBC News",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 76,
                    url = "https://tvsen5.aynaott.com/dbcnews/index.m3u8",
                    label = "AynaOTT DBC News"
                )
            )
        ),
        TvChannel(
            id = 69,
            name = "ATN Bangla",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 77,
                    url = "https://tvsen5.aynaott.com/atnbangla/index.m3u8",
                    label = "AynaOTT ATN Bangla"
                )
            )
        ),
        TvChannel(
            id = 70,
            name = "ATN News",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.NEWS,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 78,
                    url = "https://tvsen5.aynaott.com/atnnews/index.m3u8",
                    label = "AynaOTT ATN News"
                )
            )
        ),
        TvChannel(
            id = 71,
            name = "Channel i",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 79,
                    url = "https://tvsen5.aynaott.com/channeli/index.m3u8",
                    label = "AynaOTT Channel i HD"
                )
            )
        ),
        TvChannel(
            id = 72,
            name = "Deepto TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 80,
                    url = "https://byd77.aynaott.com/deeptotv/index.m3u8",
                    label = "AynaOTT Deepto Drama"
                )
            )
        ),
        TvChannel(
            id = 73,
            name = "Nagorik TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 81,
                    url = "https://tvsen5.aynaott.com/nagoriktv/index.m3u8",
                    label = "AynaOTT Nagorik HD"
                )
            )
        ),
        TvChannel(
            id = 74,
            name = "Desh TV",
            country = "Bangladesh",
            countryCode = "bd",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "IPTV-Org BD API",
            streams = listOf(
                StreamSource(
                    id = 82,
                    url = "https://tvsen5.aynaott.com/deshtv/index.m3u8",
                    label = "AynaOTT Desh TV"
                )
            )
        ),

        // ================= GLOBAL ANIME, SPORTS, SCI-TECH & NEWS (Free Public APIs) =================
        TvChannel(
            id = 75,
            name = "RetroCrush Anime TV",
            country = "United States",
            countryCode = "us",
            category = ChannelCategory.KIDS,
            apiOrigin = "Global Anime Stream API",
            streams = listOf(
                StreamSource(
                    id = 83,
                    url = "https://amg00711-cinedigm-retrocrush-cineverse-x74d5.amagi.tv/playlist/amg00711-cinedigm-retrocrush-cineverse/playlist.m3u8",
                    label = "Amagi RetroCrush Anime HD"
                )
            )
        ),
        TvChannel(
            id = 76,
            name = "NHK World Japan HD",
            country = "Japan",
            countryCode = "jp",
            category = ChannelCategory.NEWS,
            apiOrigin = "Free-TV Global API",
            streams = listOf(
                StreamSource(
                    id = 84,
                    url = "https://nhkwlive-ojp.akamaized.net/hls/live/2003459/nhkwlive-ojp-en/index.m3u8",
                    label = "NHK Tokyo Official HD"
                )
            )
        ),
        TvChannel(
            id = 77,
            name = "Red Bull TV Global",
            country = "United States",
            countryCode = "us",
            category = ChannelCategory.SPORTS,
            apiOrigin = "Global Sports Arena API",
            streams = listOf(
                StreamSource(
                    id = 85,
                    url = "https://rbmn-live.akamaized.net/hls/live/590964/BoRB-AT/master.m3u8",
                    label = "RedBull Extreme Sports 1080p"
                )
            )
        ),
        TvChannel(
            id = 78,
            name = "NASA TV Public HD",
            country = "United States",
            countryCode = "us",
            category = ChannelCategory.SCIENCE,
            apiOrigin = "Free-TV Global API",
            streams = listOf(
                StreamSource(
                    id = 86,
                    url = "https://ntv1.akamaized.net/hls/live/2014075/NASA-NTV1-HLS/master.m3u8",
                    label = "NASA Space Station Official"
                )
            )
        ),
        TvChannel(
            id = 79,
            name = "Al Jazeera English HD",
            country = "Qatar",
            countryCode = "qa",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 87,
                    url = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8",
                    label = "Al Jazeera Global CDN"
                )
            )
        ),
        TvChannel(
            id = 80,
            name = "DW English HD",
            country = "Germany",
            countryCode = "de",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 88,
                    url = "https://dwamdstream102.akamaized.net/hls/live/2015525/dwstream102/index.m3u8",
                    label = "Deutsche Welle Akamai HD"
                )
            )
        ),
        TvChannel(
            id = 81,
            name = "France 24 English",
            country = "France",
            countryCode = "fr",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 89,
                    url = "https://static.france24.com/live/F24_EN_HI_HLS/live_web.m3u8",
                    label = "France24 Paris Official"
                )
            )
        ),
        TvChannel(
            id = 82,
            name = "CNA International",
            country = "Singapore",
            countryCode = "sg",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 90,
                    url = "https://d2e1asnsl7br7b.cloudfront.net/7782e205e72f43aeb4a48ec97f66ebbe/index.m3u8",
                    label = "Channel NewsAsia Singapore"
                )
            )
        ),
        TvChannel(
            id = 83,
            name = "Arirang World Korea",
            country = "South Korea",
            countryCode = "kr",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "Free-TV Global API",
            streams = listOf(
                StreamSource(
                    id = 91,
                    url = "https://amdlive-ch01-ctnd-com.akamaized.net/arirang_1ch/smil:arirang_1ch.smil/playlist.m3u8",
                    label = "Arirang Seoul K-Culture HD"
                )
            )
        ),
        TvChannel(
            id = 84,
            name = "KBS World HD",
            country = "South Korea",
            countryCode = "kr",
            category = ChannelCategory.ENTERTAINMENT,
            apiOrigin = "Free-TV Global API",
            streams = listOf(
                StreamSource(
                    id = 92,
                    url = "https://kbsworld-ott.akamaized.net/hls/live/2002341/kbsworld/01.m3u8",
                    label = "KBS World Seoul Official"
                )
            )
        ),
        TvChannel(
            id = 85,
            name = "Bloomberg TV Global",
            country = "United States",
            countryCode = "us",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 93,
                    url = "https://liveproduseast.global.ssl.fastly.net/us/Channel-USTV-AWS-virginia-1/Source-USTV-1000-1_live.m3u8",
                    label = "Bloomberg Fastly US East"
                )
            )
        ),
        TvChannel(
            id = 86,
            name = "Sky News International",
            country = "United Kingdom",
            countryCode = "gb",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 94,
                    url = "https://linear417-gb-hls1-prd-ak.cdn.skycdp.com/100e/Content/HLS_001_1080_30/Live/channel(skynews)/index_1080-30.m3u8",
                    label = "SkyCDP 1080p UK"
                )
            )
        ),
        TvChannel(
            id = 87,
            name = "TRT World HD",
            country = "Turkey",
            countryCode = "tr",
            category = ChannelCategory.NEWS,
            apiOrigin = "World Breaking News API",
            streams = listOf(
                StreamSource(
                    id = 95,
                    url = "https://tv-trtworld.medya.trt.com.tr/master.m3u8",
                    label = "TRT Official Medya HD"
                )
            )
        )
    )
}
