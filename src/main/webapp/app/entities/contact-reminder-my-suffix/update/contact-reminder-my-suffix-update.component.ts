import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContactReminderMySuffix, ContactReminderMySuffix } from '../contact-reminder-my-suffix.model';
import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';

@Component({
  selector: 'jhl-contact-reminder-my-suffix-update',
  templateUrl: './contact-reminder-my-suffix-update.component.html',
})
export class ContactReminderMySuffixUpdateComponent implements OnInit {
  isSaving = false;

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
  });

  constructor(
    protected contactReminderService: ContactReminderMySuffixService,
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactReminderMySuffix>>): void {
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

  protected updateForm(contactReminder: IContactReminderMySuffix): void {
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
    });
  }

  protected createFromForm(): IContactReminderMySuffix {
    return {
      ...new ContactReminderMySuffix(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      contactId: this.editForm.get(['contactId'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      dateTime: this.editForm.get(['dateTime'])!.value ? dayjs(this.editForm.get(['dateTime'])!.value, DATE_TIME_FORMAT) : undefined,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      intervalValue: this.editForm.get(['intervalValue'])!.value,
      intervalType: this.editForm.get(['intervalType'])!.value,
    };
  }
}
