import { Component, OnInit } from '@angular/core';
import { WatchlistService } from "../_services/watchlist.service";
import { TokenStorageService } from "../_services/token-storage.service";
import { Watchlist } from "../models/watchlist";


@Component({
  selector: 'app-board-user',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

  currentUser: any;
  watchlist?: Watchlist[];
  statusList = ['watching', 'completed', 'dropped', 'plan_to_watch', 'on_hold'];
  status = 'watching';

  constructor(private watchlistService: WatchlistService, private token: TokenStorageService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.getWatchlist(this.currentUser.username);
  }

  getWatchlist(username:string){
    this.watchlistService.getWatchlist(username).subscribe(
      data => {
        this.watchlist = data;
      },
      err => {
        this.watchlist = JSON.parse(err.error).message;
      }
    );
  }

  getWatchlistByStatus(username:string, status:string){
    this.watchlistService.getWatchlistByStatus(username, status).subscribe(
      data => {
        this.watchlist = data;
      },
      err => {
        this.watchlist = JSON.parse(err.error).message;
      }
    );
  }

  searchByStatus(event:any) {
    this.status = event.target.value;
    console.log(" status => " + event.target.value);
    this.getWatchlistByStatus(this.currentUser.username, status);
  }
}
