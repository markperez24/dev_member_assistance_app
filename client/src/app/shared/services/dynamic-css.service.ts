import { ElementRef, Injectable } from '@angular/core';

@Injectable({providedIn: 'root'})
export class DynamicCssService {
  constructor(private el: ElementRef){
  }

  //scaleFontSize(txt) {
    /*let container = document.getElementById(element);

    // Reset font-size to 100% to begin
    container.style.fontSize = '100%';

    // Check if the text is wider than its container,
    // if so then reduce font-size
    if (container.scrollWidth > container.clientWidth) {
      container.style.fontSize = '70%';
    }*/
  //}
}
