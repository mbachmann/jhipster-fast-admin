import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentLetterFaDetailComponent } from './document-letter-fa-detail.component';

describe('Component Tests', () => {
  describe('DocumentLetterFa Management Detail Component', () => {
    let comp: DocumentLetterFaDetailComponent;
    let fixture: ComponentFixture<DocumentLetterFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DocumentLetterFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ documentLetter: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DocumentLetterFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentLetterFaDetailComponent);
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
