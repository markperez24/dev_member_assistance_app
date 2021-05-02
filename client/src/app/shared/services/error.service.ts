import { ErrorHandler, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../dialogs/error-dialog/error-dialog.component';
import { map, take } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({providedIn: 'root'})
export class ErrorService implements ErrorHandler {
  private dialogRef: MatDialogRef<ErrorDialogComponent> | undefined;

  constructor(private dialog: MatDialog,
              private router: Router) {
  }

  // Handling http errors
  handleError(err: HttpErrorResponse) {
    const errMsg = (err.message) ? err.message :
      err.status ? `${err.status} - ${err.statusText}` : 'Server error';
    let errorMessage;
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = `An error occurred: ${err.error.message}`;
    }
    //api error handling on the server
    else if (err.error && err.error.error) {
      errorMessage = `${err.error.error}`;
    } else {
      // The backend returned an unsuccessful un-handle response code.
      errorMessage = 'Something went wrong!';
    }
    if (this.dialogRef == undefined) {
      this.openWarningDialog({error: errorMessage})
        .subscribe(() => {
          this.dialogRef = undefined;
        });
      if(err.error.home) {
        this.router.navigate(['/home']);
      }
    }
  }

  private openWarningDialog(data: any): Observable<any> {
    this.dialogRef = this.dialog.open(ErrorDialogComponent,
      {data: {message: data.error}}
    );
    return this.dialogRef.afterClosed().pipe(take(1),
      map(res => {
          return res;
        }
      ));
  }

  parseBlobError(err: HttpErrorResponse) {
    const reader: FileReader = new FileReader();
    reader.onloadend = (e) => {
        const message: string = reader.result as string;
      console.log(message);
      if (this.dialogRef == undefined) {
        this.openWarningDialog({error: message})
          .subscribe(() => {
            this.dialogRef = undefined;
          });
      }
    };
    reader.readAsText(err.error);
  }
}
