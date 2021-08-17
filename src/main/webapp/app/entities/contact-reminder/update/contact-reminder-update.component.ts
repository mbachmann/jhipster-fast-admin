import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContactReminder, ContactReminder } from '../contact-reminder.model';
import { ContactReminderService } from '../service/contact-reminder.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'fa-contact-reminder-update',
  templateUrl: './contact-reminder-update.component.html',
})
export class ContactReminderUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    contactId: [],
    contactName: [],
    dateTime: [],
    title: [],
    description: [],
    intervalValue: [],
    intervalType: [],
    inactiv: [],
    contact: [],
  });

  constructor(
    protected contactReminderService: ContactReminderService,
    protected contactService: ContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactReminder }) => {
      if (contactReminder.id === undefined) {
        const today = dayjs().startOf('day');
        contactReminder.dateTime = today;
      }

      this.updateForm(contactReminder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactReminder = this.createFromForm();
    if (contactReminder.id !== undefined) {
      this.subscribeToSaveResponse(this.contactReminderService.update(contactReminder));
    } else {
      this.subscribeToSaveResponse(this.contactReminderService.create(contactReminder));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactReminder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contactReminder: IContactReminder): void {
    this.editForm.patchValue({
      id: contactReminder.id,
      remoteId: contactReminder.remoteId,
      contactId: contactReminder.contactId,
      contactName: contactReminder.contactName,
      dateTime: contactReminder.dateTime ? contactReminder.dateTime.format(DATE_TIME_FORMAT) : null,
      title: contactReminder.title,
      description: contactReminder.description,
      intervalValue: contactReminder.intervalValue,
      intervalType: contactReminder.intervalType,
      inactiv: contactReminder.inactiv,
      contact: contactReminder.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      contactReminder.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) => this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('contact')!.value))
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IContactReminder {
    return {
      ...new ContactReminder(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      contactId: this.editForm.get(['contactId'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      dateTime: this.editForm.get(['dateTime'])!.value ? dayjs(this.editForm.get(['dateTime'])!.value, DATE_TIME_FORMAT) : undefined,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      intervalValue: this.editForm.get(['intervalValue'])!.value,
      intervalType: this.editForm.get(['intervalType'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
