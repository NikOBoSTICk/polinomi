package it.niko.mywatchlist.services;

import it.niko.mywatchlist.entities.*;
import it.niko.mywatchlist.payload.request.SeriesRequest;
import it.niko.mywatchlist.repositories.GenreRepository;
import it.niko.mywatchlist.repositories.SeriesRepository;
import it.niko.mywatchlist.repositories.WatchlistRepository;
import it.niko.mywatchlist.support.exceptions.SeriesAlreadyExistsException;
import it.niko.mywatchlist.support.exceptions.SeriesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class SeriesService {
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Transactional
    public void addSeries(SeriesRequest seriesRequest) throws SeriesAlreadyExistsException {
        if(seriesRepository.existsByTitle(seriesRequest.getTitle()))
            throw new SeriesAlreadyExistsException();

        Series series = new Series(seriesRequest.getTitle());
        series.setEpisodes(seriesRequest.getEpisodes());
        series.setPlot(seriesRequest.getPlot());
        Set<String> strGenres = seriesRequest.getGenres();
        if(strGenres != null){
            Set<Genre> genres = new HashSet<>();
            for(String strGen: strGenres){
                genres.add(convertGenre(strGen));
            }
            series.setGenres(genres);
        }
        seriesRepository.save(series);
    }

    //da controllare
    @Transactional
    public void removeSeries(String title) throws SeriesNotFoundException {
        Series series = seriesRepository.findByTitle(title).orElseThrow(SeriesNotFoundException::new);
        List<Watchlist> toDel = watchlistRepository.findBySeries(series);
        watchlistRepository.deleteAll(toDel);
        seriesRepository.delete(series);
    }

    @Transactional
    public void updateSeries(SeriesRequest seriesRequest) throws SeriesNotFoundException {
        Series series = seriesRepository.findByTitle(seriesRequest.getTitle()).orElseThrow(SeriesNotFoundException::new);
        series.setEpisodes(seriesRequest.getEpisodes());
        series.setPlot(seriesRequest.getPlot());
        Set<String> strGenres = seriesRequest.getGenres();
        if(strGenres != null){
            Set<Genre> genres = new HashSet<>();
            for(String strGen: strGenres){
                genres.add(convertGenre(strGen));
            }
            series.setGenres(genres);
        }
        seriesRepository.save(series);
    }

    @Transactional(readOnly = true)
    public Page<Series> showAllSeries(int pageNumber, int pageSize, String sortBy){
        Pageable paging;
        if(sortBy.equals("title")) paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        else paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return seriesRepository.findAll(paging);
    }

    @Transactional(readOnly = true)
    public Series showSeriesById(int id) throws SeriesNotFoundException {
        return seriesRepository.findById(id).orElseThrow(SeriesNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByRating(double rating, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByRatingLessThanEqual(rating, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Series> showSeriesByViews(int minViews, int maxViews, int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        Page<Series> pagedResult = seriesRepository.findByViewsBetween(minViews, maxViews, paging);
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public Page<Series> showSeriesByTitle(String title, int pageNumber, int pageSize, String sortBy){
        Pageable paging;
        if(sortBy.equals("title")) paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        else paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return seriesRepository.findByTitleContaining(title, paging);
    }

    @Transactional(readOnly = true)
    public Page<Series> showSeriesByGenres(String gen, int pageNumber, int pageSize, String sortBy){
        Genre genre = convertGenre(gen);
        Pageable paging;
        if(sortBy.equals("title")) paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        else paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return seriesRepository.findByGenresContains(genre, paging);
    }

    private Genre convertGenre(String genre){
        switch(genre) {
            case "Drama":
                return genreRepository.findByName(EGenre.DRAMA)
                        .orElseThrow(() -> new RuntimeException("Error: Genre drama not found."));
            case "Crime":
                return genreRepository.findByName(EGenre.CRIME)
                        .orElseThrow(() -> new RuntimeException("Error: Genre crime not found."));
            case "Comedy":
                return genreRepository.findByName(EGenre.COMEDY)
                        .orElseThrow(() -> new RuntimeException("Error: Genre comedy not found."));
            case "Horror":
                return genreRepository.findByName(EGenre.HORROR)
                        .orElseThrow(() -> new RuntimeException("Error: Genre horror not found."));
            case "Fantasy":
                return genreRepository.findByName(EGenre.FANTASY)
                        .orElseThrow(() -> new RuntimeException("Error: Genre fantasy not found."));
            case "Thriller":
                return genreRepository.findByName(EGenre.THRILLER)
                        .orElseThrow(() -> new RuntimeException("Error: Genre thriller not found."));
            case "Action":
                return genreRepository.findByName(EGenre.ACTION)
                        .orElseThrow(() -> new RuntimeException("Error: Genre action not found."));
            case "Adventure":
                return genreRepository.findByName(EGenre.ADVENTURE)
                        .orElseThrow(() -> new RuntimeException("Error: Genre adventure not found."));
            case "Animation":
                return genreRepository.findByName(EGenre.ANIMATION)
                        .orElseThrow(() -> new RuntimeException("Error: Genre animation not found."));
            case "Sitcom":
                return genreRepository.findByName(EGenre.SITCOM)
                        .orElseThrow(() -> new RuntimeException("Error: Genre adventure not found."));
            case "Sci fi":
                return genreRepository.findByName(EGenre.SCI_FI)
                        .orElseThrow(() -> new RuntimeException("Error: Genre sci-fi not found."));
            case "Reality":
                return genreRepository.findByName(EGenre.REALITY)
                        .orElseThrow(() -> new RuntimeException("Error: Genre reality not found."));
            case "Soap opera":
                return genreRepository.findByName(EGenre.SOAP_OPERA)
                        .orElseThrow(() -> new RuntimeException("Error: Genre sci-fi not found."));
            case "Telenovela":
                return genreRepository.findByName(EGenre.TELENOVELA)
                        .orElseThrow(() -> new RuntimeException("Error: Genre telenovela not found."));
            case "Documentary":
                return genreRepository.findByName(EGenre.DOCUMENTARY)
                        .orElseThrow(() -> new RuntimeException("Error: Genre documentary not found."));
            case "Educational":
                return genreRepository.findByName(EGenre.EDUCATIONAL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre educational not found."));
            case "Sport":
                return genreRepository.findByName(EGenre.SPORTS)
                        .orElseThrow(() -> new RuntimeException("Error: Genre sport not found."));
            case "Romance":
                return genreRepository.findByName(EGenre.ROMANCE)
                        .orElseThrow(() -> new RuntimeException("Error: Genre romance not found."));
            case "Satirical":
                return genreRepository.findByName(EGenre.SATIRICAL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre satirical not found."));
            case "Supernatural":
                return genreRepository.findByName(EGenre.SUPERNATURAL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre supernatural not found."));
            case "Science":
                return genreRepository.findByName(EGenre.SCIENCE)
                        .orElseThrow(() -> new RuntimeException("Error: Genre science not found."));
            case "School":
                return genreRepository.findByName(EGenre.SCHOOL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre school not found."));
            case "Time travel":
                return genreRepository.findByName(EGenre.TIME_TRAVEL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre time_travel not found."));
            case "Historical":
                return genreRepository.findByName(EGenre.HISTORICAL)
                        .orElseThrow(() -> new RuntimeException("Error: Genre historical not found."));
            case "War":
                return genreRepository.findByName(EGenre.WAR)
                        .orElseThrow(() -> new RuntimeException("Error: Genre war not found."));
            default:
                throw new RuntimeException("Error: Genre not found.");
        }
    }
}
