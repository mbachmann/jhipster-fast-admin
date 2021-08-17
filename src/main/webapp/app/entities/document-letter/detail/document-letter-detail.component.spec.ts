import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentLetterDetailComponent } from './document-letter-detail.component';

describe('Component Tests', () => {
  describe('DocumentLetter Management Detail Component', () => {
    let comp: DocumentLetterDetailComponent;
    let fixture: ComponentFixture<DocumentLetterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentLetterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentLetter: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentLetterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentLetterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load documentLetter on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.documentLetter).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
