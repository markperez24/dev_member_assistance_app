import {
  Component,
  Input,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
  EventEmitter,
  ElementRef,
  AfterViewInit
} from '@angular/core';
import { MemberAssistanceApi } from '../../../shared/constants/member.assistance.api';
import { RegistrationService } from '../../../shared/services/registration.service';

@Component({
  selector: 'app-registration-video',
  templateUrl: './registration-video.component.html',
  styleUrls: ['./registration-video.component.scss']
})
export class RegistrationVideoComponent implements AfterViewInit {
  @Output() videoEnded = new EventEmitter<boolean>();
  @ViewChild('player') player: any;
  videoId: string = '';
  width: any;
  windows: any;
  videoUrl: any;
  videoMessage: any;
  playerVars: any;
 // @ViewChild("videoRef") private videoRef!: ElementRef<HTMLVideoElement>;

  constructor(private registrationService: RegistrationService) {
    this.playerVars = {
      'autoplay': 1,
      'controls': 0,
      'playsinline': 1,
      'disablekb': 1
    };
  }

  ngAfterViewInit(): void {
    this.getVideoDetails();
  }

  getVideoDetails() {
    this.registrationService.getVideoDetails(
      MemberAssistanceApi.GET_VIDEO_DETAILS, null)
      .subscribe(result => {
        if (result) {
          this.videoMessage = result.videoMessage;
          this.videoUrl = result.videoUrl;
          this.loadVideo(result);
        } else {
          console.log('Cannot get video url.');
        }
      });
  }

  loadVideo(result: any) {
    /*this.videoId = result.videoUrlId;
    const tag = document.createElement('script');
    tag.src = 'https://www.youtube.com/iframe_api';
    document.body.appendChild(tag);
    localStorage.setItem('initRegistration', new Date().toISOString());*/
    /*let video: any = document.getElementById('video');
    let supposedCurrentTime = 0;
    if(video) {
      video.addEventListener('timeupdate', function () {
        if (!video.seeking) {
          supposedCurrentTime = video.currentTime;
        }
      });
      // prevent user from seeking
      video.addEventListener('seeking', function () {
        // guard agains infinite recursion:
        // user seeks, seeking is fired, currentTime is modified, seeking is fired, current time is modified, ....
        let delta = video.currentTime - supposedCurrentTime;
        if (Math.abs(delta) > 0.01) {
          video.currentTime = supposedCurrentTime;
        }
      });
      // delete the following event handler if rewind is not required
      video.addEventListener('ended', function () {
        // reset state in order to allow for rewind
        supposedCurrentTime = 0;
        //document.onStateChange();
      });
    }*/
  }

  // Autoplay
  onReady(event: any) {
    this.player.playVideo();
  }

  // Loop
  onStateChange(event: any) {
    if (event.data === 0) {
      this.videoEnded.emit(true);
    }
  }

  onEnd(event: any) {
    this.videoEnded.emit(true);
  }
}
