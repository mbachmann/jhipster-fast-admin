import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentFreeTextDetailComponent } from './document-free-text-detail.component';

describe('Component Tests', () => {
  describe('DocumentFreeText Management Detail Component', () => {
    let comp: DocumentFreeTextDetailComponent;
    let fixture: ComponentFixture<DocumentFreeTextDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentFreeTextDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentFreeText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentFreeTextDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentFreeTextDetailComponent);
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
