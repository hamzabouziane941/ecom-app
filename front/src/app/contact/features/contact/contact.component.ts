import { Component, ViewEncapsulation } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ContactService } from '../data-acess/contact.service';
import { ContactDetail } from '../data-acess/contact.model';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    InputTextModule,
    InputTextareaModule,
    ToastModule
  ],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css',
  encapsulation: ViewEncapsulation.None,
  providers: [MessageService]
})
export class ContactComponent {
  email: string = '';
  message: string = '';
  isSubmitting = false;
  hasSubmitted = false;

  constructor(
    private contactService: ContactService,
    private messageService: MessageService
  ) {}

  onSubmit() {
    if (this.email && this.message && this.message.length <= 300) {
      this.isSubmitting = true;
      this.hasSubmitted = false;
      
      const formData: ContactDetail = {
        email: this.email,
        message: this.message
      };

      this.contactService.submitContactForm(formData).subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Succès',
            detail: response.message
          });
          this.resetForm();
          this.hasSubmitted = true;
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
    }
  }

  private resetForm() {
    this.email = '';
    this.message = '';
  }

  shouldShowValidation(input: any): boolean {
    return input.invalid && input.touched && !this.hasSubmitted;
  }
} 