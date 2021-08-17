import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContactGroup, ContactGroup } from '../contact-group.model';
import { ContactGroupService } from '../service/contact-group.service';

@Component({
  selector: 'fa-contact-group-update',
  templateUrl: './contact-group-update.component.html',
})
export class ContactGroupUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [null, [Validators.required]],
    usage: [],
  });

  constructor(protected contactGroupService: ContactGroupService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactGroup }) => {
      this.updateForm(contactGroup);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactGroup>>): void {
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

  protected updateForm(contactGroup: IContactGroup): void {
    this.editForm.patchValue({
      id: contactGroup.id,
      remoteId: contactGroup.remoteId,
      name: contactGroup.name,
      usage: contactGroup.usage,
    });
  }

  protected createFromForm(): IContactGroup {
    return {
      ...new ContactGroup(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      usage: this.editForm.get(['usage'])!.value,
    };
  }
}
