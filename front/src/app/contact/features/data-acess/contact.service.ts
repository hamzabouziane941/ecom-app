import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { ContactDetail, ContactResponse } from './contact.model';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  submitContactForm(form: ContactDetail): Observable<ContactResponse> {
  
    return of({
      success: true,
      message: 'Demande de contact envoyée avec succès'
    }).pipe(
      delay(1000)
    );
  }
} 