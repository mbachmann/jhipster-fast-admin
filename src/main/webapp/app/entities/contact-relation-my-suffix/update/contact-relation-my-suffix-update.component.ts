import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContactRelationMySuffix, ContactRelationMySuffix } from '../contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';

@Component({
  selector: 'jhl-contact-relation-my-suffix-update',
  templateUrl: './contact-relation-my-suffix-update.component.html',
})
export class ContactRelationMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    contactRelationType: [],
  });

  constructor(
    protected contactRelationService: ContactRelationMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactRelation }) => {
      this.updateForm(contactRelation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactRelation = this.createFromForm();
    if (contactRelation.id !== undefined) {
      this.subscribeToSaveResponse(this.contactRelationService.update(contactRelation));
    } else {
      this.subscribeToSaveResponse(this.contactRelationService.create(contactRelation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactRelationMySuffix>>): void {
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

  protected updateForm(contactRelation: IContactRelationMySuffix): void {
    this.editForm.patchValue({
      id: contactRelation.id,
      contactRelationType: contactRelation.contactRelationType,
    });
  }

  protected createFromForm(): IContactRelationMySuffix {
    return {
      ...new ContactRelationMySuffix(),
      id: this.editForm.get(['id'])!.value,
      contactRelationType: this.editForm.get(['contactRelationType'])!.value,
    };
  }
}
