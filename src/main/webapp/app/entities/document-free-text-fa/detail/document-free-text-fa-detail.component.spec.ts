import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentFreeTextFaDetailComponent } from './document-free-text-fa-detail.component';

describe('Component Tests', () => {
  describe('DocumentFreeTextFa Management Detail Component', () => {
    let comp: DocumentFreeTextFaDetailComponent;
    let fixture: ComponentFixture<DocumentFreeTextFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentFreeTextFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentFreeText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentFreeTextFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentFreeTextFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentFreeText on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentFreeText).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
