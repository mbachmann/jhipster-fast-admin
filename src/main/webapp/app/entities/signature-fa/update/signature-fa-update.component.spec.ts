jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SignatureFaService } from '../service/signature-fa.service';
import { ISignatureFa, SignatureFa } from '../signature-fa.model';
import { IApplicationUserFa } from 'app/entities/application-user-fa/application-user-fa.model';
import { ApplicationUserFaService } from 'app/entities/application-user-fa/service/application-user-fa.service';

import { SignatureFaUpdateComponent } from './signature-fa-update.component';

describe('Component Tests', () => {
  describe('SignatureFa Management Update Component', () => {
    let comp: SignatureFaUpdateComponent;
    let fixture: ComponentFixture<SignatureFaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let signatureService: SignatureFaService;
    let applicationUserService: ApplicationUserFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SignatureFaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SignatureFaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignatureFaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      signatureService = TestBed.inject(SignatureFaService);
      applicationUserService = TestBed.inject(ApplicationUserFaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApplicationUserFa query and add missing value', () => {
        const signature: ISignatureFa = { id: 456 };
        const applicationUser: IApplicationUserFa = { id: 84732 };
        signature.applicationUser = applicationUser;

        const applicationUserCollection: IApplicationUserFa[] = [{ id: 99915 }];
        jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
        const additionalApplicationUserFas = [applicationUser];
        const expectedCollection: IApplicationUserFa[] = [...additionalApplicationUserFas, ...applicationUserCollection];
        jest.spyOn(applicationUserService, 'addApplicationUserFaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ signature });
        comp.ngOnInit();

        expect(applicationUserService.query).toHaveBeenCalled();
        expect(applicationUserService.addApplicationUserFaToCollectionIfMissing).toHaveBeenCalledWith(
          applicationUserCollection,
          ...additionalApplicationUserFas
        );
        expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const signature: ISignatureFa = { id: 456 };
        const applicationUser: IApplicationUserFa = { id: 77203 };
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
        const saveSubject = new Subject<HttpResponse<SignatureFa>>();
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
        const saveSubject = new Subject<HttpResponse<SignatureFa>>();
        const signature = new SignatureFa();
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
        const saveSubject = new Subject<HttpResponse<SignatureFa>>();
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
      describe('trackApplicationUserFaById', () => {
        it('Should return tracked ApplicationUserFa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicationUserFaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
