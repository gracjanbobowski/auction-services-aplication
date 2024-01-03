package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

//AuctionService: Logika biznesowa związana z aukcjami.
@Service
public class AuctionService {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<Auction> getAll() {
        return jdbcTemplate.query("SELECT id, category, title",
                BeanPropertyRowMapper.newInstance(Auction.class));
    }

    public Auction getById(int id) {
        return jdbcTemplate.queryForObject("SELECT id, category, title"
                + "id = ?", BeanPropertyRowMapper.newInstance(Auction.class));
    }

//    public int save(List<Auction> auctions) {
//        auctions.forEach(auction -> jdbcTemplate.update("INSERT INTO auction(category, id) VALLUES(?, ?)",
//                auction.getCategory(), auction.getId));
//
//    }

}

