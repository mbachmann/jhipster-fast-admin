import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';
import { ContactGroupFaService } from '../service/contact-group-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

@Component({
  selector: 'fa-contact-group-fa-update',
  templateUrl: './contact-group-fa-update.component.html',
})
export class ContactGroupFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    contact: [],
  });

  constructor(
    protected contactGroupService: ContactGroupFaService,
    protected contactService: ContactFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactGroup }) => {
      this.updateForm(contactGroup);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactGroup = this.createFromForm();
    if (contactGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.contactGroupService.update(contactGroup));
    } else {
      this.subscribeToSaveResponse(this.contactGroupService.create(contactGroup));
    }
  }

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactGroupFa>>): void {
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

  protected updateForm(contactGroup: IContactGroupFa): void {
    this.editForm.patchValue({
      id: contactGroup.id,
      name: contactGroup.name,
      contact: contactGroup.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      contactGroup.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContactFa[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContactFa[]) =>
          this.contactService.addContactFaToCollectionIfMissing(contacts, this.editForm.get('contact')!.value)
        )
      )
      .subscribe((contacts: IContactFa[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IContactGroupFa {
    return {
      ...new ContactGroupFa(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
