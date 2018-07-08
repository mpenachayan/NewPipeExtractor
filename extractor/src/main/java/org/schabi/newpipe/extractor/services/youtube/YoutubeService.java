package org.schabi.newpipe.extractor.services.youtube;

import org.schabi.newpipe.extractor.*;
import org.schabi.newpipe.extractor.uih.*;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.youtube.extractors.*;
import org.schabi.newpipe.extractor.services.youtube.urlIdHandlers.*;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;

import static java.util.Arrays.asList;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.*;


/*
 * Created by Christian Schabesberger on 23.08.15.
 *
 * Copyright (C) Christian Schabesberger 2018 <chris.schabesberger@mailbox.org>
 * YoutubeService.java is part of NewPipe.
 *
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

public class YoutubeService extends StreamingService {

    public YoutubeService(int id) {
        super(id, "YouTube", asList(AUDIO, VIDEO, LIVE));
    }

    @Override
    public SearchExtractor getSearchExtractor(SearchQIHandler query, String contentCountry) {
        return new YoutubeSearchExtractor(this, query, contentCountry);
    }

    @Override
    public UIHFactory getStreamUIHFactory() {
        return YoutubeStreamUIHFactory.getInstance();
    }

    @Override
    public ListUIHFactory getChannelUIHFactory() {
        return YoutubeChannelUIHFactory.getInstance();
    }

    @Override
    public ListUIHFactory getPlaylistUIHFactory() {
        return YoutubePlaylistUIHFactory.getInstance();
    }

    @Override
    public SearchQIHFactory getSearchQIHFactory() {
        return YoutubeSearchQIHFactory.getInstance();
    }

    @Override
    public StreamExtractor getStreamExtractor(UIHandler uiHandler) throws ExtractionException {
        return new YoutubeStreamExtractor(this, uiHandler);
    }

    @Override
    public ChannelExtractor getChannelExtractor(ListUIHandler urlIdHandler) throws ExtractionException {
        return new YoutubeChannelExtractor(this, urlIdHandler);
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(ListUIHandler urlIdHandler) throws ExtractionException {
        return new YoutubePlaylistExtractor(this, urlIdHandler);
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new YoutubeSuggestionExtractor(getServiceId());
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        KioskList list = new KioskList(getServiceId());

        // add kiosks here e.g.:
        try {
            list.addKioskEntry(new KioskList.KioskExtractorFactory() {
                @Override
                public KioskExtractor createNewKiosk(StreamingService streamingService, String url, String id)
                throws ExtractionException {
                    return new YoutubeTrendingExtractor(YoutubeService.this,
                            new YoutubeTrendingUIHFactory().fromUrl(url), id);
                }
            }, new YoutubeTrendingUIHFactory(), "Trending");
            list.setDefaultKiosk("Trending");
        } catch (Exception e) {
            throw new ExtractionException(e);
        }

        return list;
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return new YoutubeSubscriptionExtractor(this);
    }

}