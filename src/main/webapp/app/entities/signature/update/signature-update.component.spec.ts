jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SignatureService } from '../service/signature.service';
import { ISignature, Signature } from '../signature.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { SignatureUpdateComponent } from './signature-update.component';

describe('Component Tests', () => {
  describe('Signature Management Update Component', () => {
    let comp: SignatureUpdateComponent;
    let fixture: ComponentFixture<SignatureUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let signatureService: SignatureService;
    let applicationUserService: ApplicationUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SignatureUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SignatureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignatureUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      signatureService = TestBed.inject(SignatureService);
      applicationUserService = TestBed.inject(ApplicationUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApplicationUser query and add missing value', () => {
        const signature: ISignature = { id: 456 };
        const applicationUser: IApplicationUser = { id: 84732 };
        signature.applicationUser = applicationUser;

        const applicationUserCollection: IApplicationUser[] = [{ id: 99915 }];
        jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
        const additionalApplicationUsers = [applicationUser];
        const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
        jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        expect(applicationUserService.query).toHaveBeenCalled();
        expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
          applicationUserCollection,
          ...additionalApplicationUsers
        );
        expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const signature: ISignature = { id: 456 };
        const applicationUser: IApplicationUser = { id: 77203 };
        signature.applicationUser = applicationUser;

        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(signature));
        expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Signature>>();
        const signature = { id: 123 };
        jest.spyOn(signatureService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: signature }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(signatureService.update).toHaveBeenCalledWith(signature);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Signature>>();
        const signature = new Signature();
        jest.spyOn(signatureService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: signature }));
        saveSubject.complete();

        // THEN
        expect(signatureService.create).toHaveBeenCalledWith(signature);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Signature>>();
        const signature = { id: 123 };
        jest.spyOn(signatureService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(signatureService.update).toHaveBeenCalledWith(signature);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApplicationUserById', () => {
        it('Should return tracked ApplicationUser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicationUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
